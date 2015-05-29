<?php
include_once '../web_service/GCM/GCM.php';
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$shopID = $_POST['shop_id'];
$cardID = $_POST['card_id'];
$customerName = $_POST['username'];
/*$fullname = $_POST['fullname'];
$phone = $_POST['phone'];*/
$point = $_POST['total_points'];
/*$billCode = $_POST['billCode'];*/
$billCode = NULL;
$clientTime = $_POST['time'];
$event_list = $_POST['list_events'];
$list = json_decode($event_list);

// Kiểm tra token
if(strlen($token)!=64){
    echo '{"error":"token not found"}';
    die();
}

$query = "select username from admin_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token"}';
    die();
}
// kiểm tra xem shopID có thuộc user có user.token == token hay không
/* check exist card id in "admin_card" table*/
$query = "select * from admin_card where admin_username='".$username."' and card_id = '" . $cardID . "'";

$query_exec = mysqli_query($localhost, $query);
$card_rows = mysqli_num_rows($query_exec);

$query = "select * from card_shop where card_id='".$cardID."' and shop_id = '" . $shopID . "'";

$query_exec = mysqli_query($localhost, $query);
$shop_rows = mysqli_num_rows($query_exec);

if($card_rows == 0) {//have no shop in database
    echo '{"error":"not your card"}';
}else if($shop_rows == 0){
    echo '{"error":"not your shop"}';
}else{
    // Kiểm tra xem trong bảng customer_card có dòng nào thỏa: username == $customerName && card_id == $cardID
    $query = "select * from customer_card where username='".$customerName."' and card_id='".$cardID."'";
    $query_exec = mysqli_query($localhost, $query);
    $row = mysqli_fetch_array($query_exec);
    $cardID = $row['card_id'];

    if($cardID == ""){
        echo '{"error":"not your user"}';
        die();
    }

    // Cập nhật điểm cho user trong bảng customer_card
    $currentPoint = $row['point'];
    $newPoint = $point + $currentPoint;

    $query = "update customer_card set point = " . $newPoint . " where username='".$customerName."' and card_id='".$cardID."'";
    $query_exec = mysqli_query($localhost, $query);

    if($query_exec) {   // Cập nhật thành công

        // Lưu lần cập nhật này vào bảng update_point_history
        $bucketName = "loyalty-point-photos";
        $fileName = "shops/" . $shopID . "/history/" . $billCode;
        $imageLink = "http://storage.googleapis.com/" . $bucketName . "/" . $fileName;

        if($billCode == "" || $billCode == NULL) {
            $billCode = uniqid();
            $imageLink = NULL;
        }        

        // Lấy thông tin customer
        $query = "select * from customer_users where username='".$customerName."'";
        $query_exec = mysqli_query($localhost, $query);        
        $customer_row = mysqli_fetch_array($query_exec);
        $fullname = $customer_row['name'];
        $phone = $customer_row['phone_number'];

        $query = "insert into history values ('"
                                    .$billCode."','1','"
                                    .$customerName."','"
                                    .$fullname."','"
                                    .$phone."','"
                                    .$point."','"
                                    .$clientTime."','"
                                    .$imageLink."')";  //insert vào database

        $query_exec = mysqli_query($localhost, $query);
        if($query_exec) {
            $query = "insert into history_card_shop values ('"
                                    .$billCode."','"
                                    .$cardID."','"
                                    .$shopID."')";
            $query_exec = mysqli_query($localhost, $query);
            if($query_exec){
                //insert billcode info to database
                for($i=0; $i < sizeof($list); $i++){
                    $query = "insert into update_point_history values ('','"
                                                    .$billCode."','"
                                                    .$list[$i]->event->id."','"
                                                    .$list[$i]->quantity."','"
                                                    .$shopID."')";  //insert vào database
                    
                    $query_exec = mysqli_query($localhost, $query);
                }
                if($imageLink != NULL)
                    echo '{"error":""}';
                else
                    echo '{"error":""}';

                // Gửi notification cho user
                // Từ $customerName -> regID (bảng customer_registration)
                $query = "select * from customer_registration where username='".$customerName."'";
                $query_exec = mysqli_query($localhost, $query);
                $row = mysqli_fetch_array($query_exec);
                $regID = $row['regID']; 

                // Gửi thông báo đến regID
                if($regID != "") {

                    $regID = array($regID);
                    $message = "add point";

                    $message = array("message" => $message, "shopID" => $shopID, "point" => $point, "newPoint" => $newPoint, "historyID" => $billCode);

                    $gcm = new GCM();

                    $result = $gcm->send_notification($regID, $message);
                }
            }else{
                echo '{"error":"update history unsuccessfully"}';
            }
            
        }
        else 
            echo '{"error":"update history unsuccessfully", "bucketName":"", "fileName":""}';
    }else
        echo '{"error":"update point unsuccessfully", "bucketName":"", "fileName":""}';
}
mysqli_close($localhost);
?>