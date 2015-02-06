<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint"; 
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);

$query = $_POST['query'];

$query = str_replace("\'","'",$query);
$query_search = $query;
$query_exec = mysqli_query($localhost,$query_search) or die(mysql_error());

$rows = mysqli_num_rows($query_exec);

if($rows == 0) {
    echo "Not Found";
}
else  {
    while($row = mysqli_fetch_array($query_exec)){
        echo $row['id']."  ".$row['like'];
        echo "<br>";
    }
}
mysqli_close($localhost);
?>			