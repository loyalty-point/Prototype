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
    echo '{"error":"token not found","listAchievedEvents":[]}';
    die();
}

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
        echo '{"error":"wrong token","listAchievedEvents":[]}';
        die();
}
/**/
$query = "select * from update_point_history where history_id = '".$history_id."'";
$query_exec = mysqli_query($localhost, $query);
$result = '{"error":"", "listAchievedEvents":[';
while($row = mysqli_fetch_array($query_exec)){

    $query_search = "select * from event where id = '".$row['event_id']."'";

    $query_exec1 = mysqli_query($localhost,$query_search);

    while($row1 = mysqli_fetch_array($query_exec1)){
        $result = $result . '{"quantity":"'.$row['event_number'].
            '","event":{"id":"'.$row1['id'].
            '","type":"'.$row1['type'].
            '","name":"'.$row1['name'].
            '","time_start":"'.$row1['time_start'].
            '","time_end":"'.$row1['time_end'].
            '","description":"'.$row1['description'].
            '","barcode":"'.$row1['barcode'].
            '","goods_name":"'.$row1['goods_name'].
            '","ratio":"'.$row1['ratio'].
            '","number":"'.$row1['number'].
            '","point":"'.$row1['point'].
            '","image":"'.$row1['image'].'"}},';
    }
}
$result = $result . ']}';

echo $result;
mysqli_close($localhost);
?>