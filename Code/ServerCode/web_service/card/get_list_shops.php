<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];
$card_id = $_POST['card_id'];

if(strlen($token)!=64){
    echo '{"error":"token not found","listShops":[]}';
    die();
}

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token","listShops":[]}';
    die();
}
/**/

/* check exist shop id in "admin_shop" table*/
$query = "select * from card_shop where card_id='".$card_id."'";

$query_exec = mysqli_query($localhost, $query);
$rows = mysqli_num_rows($query_exec);

if($rows == 0) {//have no shop in database
    echo '{"error":"have no shop in this card","listShops":[]}';;
}
else  {
    $result = '{"error":"", "listShops":[';
    while($row = mysqli_fetch_array($query_exec)){
        $query_search = "select * from shop where id='".$row['shop_id']."'";

        $query_exec1 = mysqli_query($localhost,$query_search);   

        while($row1 = mysqli_fetch_array($query_exec1)){
            $result = $result . '{' 
                    . '"id":"' . $row1['id'] . '",'
                    . '"name":"' . $row1['name'] . '",'
                    . '"address":"' . $row1['address'] . '",'
                    . '"phone_number":"' . $row1['phone_number'] . '",'
                    . '"category":"' . $row1['category'] . '",'
                    . '"exchange_ratio":"' . $row1['exchange_ratio'] . '",'
                    . '"image":"' . $row1['image'] . '",'
                    . '"cardImg":"' . $row1['background'] . '"},';
        }
    }
    $result = $result . ']}';
    echo $result;
}

mysqli_close($localhost);
?>