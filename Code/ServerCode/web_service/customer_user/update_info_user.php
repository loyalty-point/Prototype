<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint"; 
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 
$token = $_POST['token'];
$user = $_POST['user'];
$user = json_decode($user);

$query_search = "select * from customer_users where token='".$token."'";

$query_exec = mysqli_query($localhost,$query_search) or die(mysql_error());

$rows = mysqli_num_rows($query_exec);

if($rows == 0) {//Không có token này trong db
    echo '{"error":"token not found"}';
}
else  {
    $query = "update customer_users " 
              . "set phone_number = '" . $user->phone
              . "', email = '" . $user->email
              . "', name = '" . $user->fullname 
              . "', address = '" . $user->address
              . "', identity_number = '" . $user->identity_number
                  . "' where username = '" . $user->username."'";

    $query_exec = mysqli_query($localhost,$query) or die(mysql_error());
    if($query_exec)
      echo '{"error":""}';
    else
      echo '{"error":"edit user info failed"}';
}
mysqli_close($localhost);
?>