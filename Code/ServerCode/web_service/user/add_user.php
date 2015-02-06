<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

$post = $_POST['user'];
$user = json_decode($post);

mysql_select_db($database_localhost, $localhost);

$query = "insert into admin_users values ('"
							.$user->username."','"
							.$user->password."','"
							.$user->phone."','"
							.$user->email."','"
							.$user->fullname."','"
							.$user->address."','"
							.$user->avatar."')"; //$_POST['query'];

$query_search = $query;
$query_exec = mysql_query($query_search) or die(mysql_error());
mysql_close($localhost);
?>