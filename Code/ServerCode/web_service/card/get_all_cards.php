<?php
$hostname_localhost ="localhost";
$database_localhost ="loyaltypoint";
$username_localhost ="root";
$password_localhost ="matrix123";
$localhost = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost, $database_localhost);
mysqli_query($localhost,"SET NAMES 'UTF8'"); 

$token = $_POST['token'];

if(strlen($token)!=64){
    echo '{"error":"token not found","listCards":[]}';
    die();
}

/* check token and return username */
$query = "select username from admin_users where token='".$token."'";

$query_exec = mysqli_query($localhost, $query);
$row = mysqli_fetch_array($query_exec);
$username = $row['username'];

if($username == ""){
    echo '{"error":"wrong token","listCards":[]}';
    die();
}
/**/

/* check exist shop id in "admin_shop" table*/
$query = "select * from admin_card where admin_username='".$username."'";

$query_exec = mysqli_query($localhost, $query);
$rows = mysqli_num_rows($query_exec);

if($rows == 0) {//have no shop in database
    echo '{"error":"have no card in database","listCards":[]}';;
}
else  {
    $result = '{"error":"", "listCards":[';
    while($row = mysqli_fetch_array($query_exec)){
        $query_search = "select * from card where id='".$row['card_id']."'";

        $query_exec1 = mysqli_query($localhost,$query_search);   

        while($row1 = mysqli_fetch_array($query_exec1)){
            $result = $result . '{' 
                    . '"id":"' . $row1['id'] . '",'
                    . '"name":"' . $row1['name'] . '",'
                    . '"image":"' . $row1['image'] . '"},';
        }
    }
    $result = $result . ']}';
    echo $result;
}

mysqli_close($localhost);
?>