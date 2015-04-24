<?php
include_once '../GCM/GCM.php';
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$shopID = $_POST['shop_id'];
$customerName = $_POST['username'];
$fullname = $_POST['fullname'];
$phone = $_POST['phone'];
$point = $_POST['point'];
$billCode = $_POST['billCode'];
$clientTime = $_POST['time'];
$event_list = $_POST['event_list'];
$list = json_decode($event_list);
// echo $list[0]->event->id;
// die();

// Kiểm tra token
if(strlen($token)!=64){
    echo '{"error":"token not found", "bucketName":"", "fileName":""}';
    die();
}

$query = "select username from admin_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token", "bucketName":"", "fileName":""}';
	die();
}
// kiểm tra xem shopID có thuộc user có user.token == token hay không
/* check exist shop id in "admin_shop" table*/
$query = "select * from admin_shop where admin_username='".$username."' and shop_id='".$shopID."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$shopID = $row['shop_id'];

if($shopID == ""){
    echo '{"error":"not your shop", "bucketName":"", "fileName":""}';
	die();
}

// Kiểm tra xem trong bảng customer_shop có dòng nào thỏa: username == $customerName && shop_id == $shopID
$query = "select * from customer_shop where username='".$customerName."' and shop_id='".$shopID."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$shopID = $row['shop_id'];

if($shopID == ""){
    echo '{"error":"not your user", "bucketName":"", "fileName":""}';
    die();
}

// Cập nhật điểm cho user trong bảng customer_shop
$currentPoint = $row['point'];
$newPoint = $point + $currentPoint;

$query = "update customer_shop set point = " . $newPoint . " where username='".$customerName."' and shop_id='".$shopID."'";
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

    $query = "insert into history values ('"
                                .$billCode."','1','"
                                .$customerName."','"
                                .$fullname."','"
                                .$phone."','"
                                .$shopID."','"
                                .$point."','"
                                .$clientTime."','"
                                .$imageLink."')";  //insert vào database

    $query_exec = mysqli_query($localhost, $query);
    if($query_exec) {
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
            echo '{"error":"", "bucketName":"' . $bucketName . '","fileName":"' . $fileName . '"}';
        else
            echo '{"error":"", "bucketName":"", "fileName":""}';
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
    }
    else 
        echo '{"error":"update history unsuccessfully", "bucketName":"", "fileName":""}';
}else
    echo '{"error":"update point unsuccessfully", "bucketName":"", "fileName":""}';

mysqli_close($localhost);
?>