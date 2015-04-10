<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$clientTime = $_POST['time'];
$shopID = $_POST['shop_id'];
$awardID = $_POST['award_id'];
$quantity = $_POST['quantity'];


// Kiểm tra token
if(strlen($token)!=64){
    echo '{"error":"token not found"}';
    die();
}

$query = "select * from customer_users where token='".$token."'";
$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token"}';
	die();
}

$userFullName = $row['name'];

// Tìm trong bảng award thỏa điều kiện
// award.awardID == $awardID && award.shopID = $shopID
$query = "select * from award where id='".$awardID."' and shopID = '" . $shopID . "'";
$query_exec = mysqli_query($localhost, $query);
$awardRow = mysqli_fetch_array($query_exec);
$awardID = $awardRow['id'];

if($awardID == "") {
    echo '{"error":"this shop does not have this award"}';
    die();
}

$awardName = $awardRow['name'];
$awardImage = $awardRow['image'];
$awardPoint = $awardRow['point'];


// Lấy thông tin của shop
$query = "select * from shop where id='".$shopID."'";
$query_exec = mysqli_query($localhost, $query);
$shopRow = mysqli_fetch_array($query_exec);
$shopID = $shopRow['id'];

if($shopID == "") {
    echo '{"error":"shop does not exist"}';
    die();
}

$shopName = $shopRow['name'];
$shopImage = $shopRow['image'];

// Thêm 1 dòng vào bảng buy_award_history
$id = uniqid();
$isTaken = 0;
$bucketName = "loyalty-point-photos";
$path = "shops/" . $id;
$imageLink = "http://storage.googleapis.com/" . $bucketName . "/" . $path . "/shopLogo";
$query = "insert into buy_award_history values ('"
                            .$id."','"
                            .$clientTime."','"
                            .$username."','"
                            .$userFullName."','"
                            .$shopID."','"
                            .$shopName."','"
                            .$imageLink."','"
                            .$awardID."','"
                            .$awardName."','"
                            .$awardImage."','"
                            .$quantity."','"
                            .$isTaken."')";  //insert vào database

$query_exec = mysqli_query($localhost, $query);
if($query_exec) {
    echo '{"error":""}';
    // update lại award quantity
    $newQuantity = $awardRow['quantity'] - $quantity;
    $query = "update award set quantity = '" . $newQuantity . "' where id = '" . $awardID . "'";
    $query_exec = mysqli_query($localhost, $query);

    // Trừ điểm tích lũy của user trong bảng customer_shop
    $point = $quantity * $awardPoint;

    $query = "select * from customer_shop where username='".$username."' and shop_id = '" . $shopID . "'";
    $query_exec = mysqli_query($localhost, $query);
    $folllowRow = mysqli_fetch_array($query_exec);
    $currentPoint = $folllowRow['point'];
    $newPoint = $currentPoint - $point;

    $query = "update customer_shop set point = '".$newPoint."' where username='".$username."' and shop_id ='".$shopID."'";
    $query_exec = mysqli_query($localhost, $query);
}
else 
    echo '{"error":"'.mysqli_error($localhost).'"}';


mysqli_close($localhost);
?>