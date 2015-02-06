<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint"; 
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);

$username = $_POST['username'];
$hashpass = $_POST['hashpass'];

$query_search = "select * from admin_users where username='".$username."'";

$query_exec = mysqli_query($localhost,$query_search) or die(mysql_error());

$rows = mysqli_num_rows($query_exec);

if($rows == 0) { //Sai tài khoản
    echo '{"error":"1","token":""}';
}
else {
    while($row = mysqli_fetch_array($query_exec)){
    	if($hashpass != $row['password']){  //Sai password
    		echo '{"error":"2","token":""}';
    	} else{ //Đúng password và sinh token (md5 của username nối vối md5 của thời gian hệ thống)
    		$time = date("F j, Y, g:i a");
    		$token = md5($username).md5($time);
    		$query_update = "update admin_users set token='".$token."' where username='".$username."'";
    		$query_exec = mysqli_query($localhost, $query_update);
			echo '{"error":"","token":"'.$token.'"}';
    	}
    }
}
mysqli_close($localhost);
?>			