<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'");

$token = $_POST['token'];   
$history_id = $_POST['history_id'];

if(strlen($token)!=64){
    echo '{"error":"token not found","award":"", "award_number":""}';
    die();
}

/* check token and return username */
$query = "select username from customer_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
        echo '{"error":"wrong token","award":"", "award_number":""}';
        die();
}
/**/
$query = "select * from buy_award_history where history_id = '".$history_id."'";
$query_exec = mysqli_query($localhost, $query);
$result = '{"error":"", "award":';
while($row = mysqli_fetch_array($query_exec)){

    $query_search = "select * from award where id = '".$row['award_id']."'";

    $query_exec1 = mysqli_query($localhost,$query_search);

    while($row1 = mysqli_fetch_array($query_exec1)){
        $result = $result . '{"id":"'.$row1['id'].
            '","name":"'.$row1['name'].
            '","point":"'.$row1['point'].
            '","quantity":"'.$row1['quantity'].
            '","description":"'.$row1['description'].
            '","image":"'.$row1['image'].
            '","shopID":"'.$row1['shopID'].'"},';
    }
    $result = $result . '"award_number":"'.$row['award_number'].'"}';
}

echo $result;
mysqli_close($localhost);
?>