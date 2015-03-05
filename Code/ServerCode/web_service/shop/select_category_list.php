<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint"; 
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);

$query_search = "select * from category_shop";
$query_exec = mysqli_query($localhost,$query_search) or die(mysql_error());

$rows = mysqli_num_rows($query_exec);

if($rows == 0) {//Không có id này trong db
    echo "";
}
else  {
    while($row = mysqli_fetch_array($query_exec)){
        echo '{"id":"'.$row['id'].
        	'","name":"'.$row['name'].'"}';
    }
}
mysqli_close($localhost);
?>