<?php
$id = $_GET["id"];
$conn = new mysqli("db", "user", "password", "appDB");
if ($id){

if (isset($_POST["name"])) {

    $name = $conn->real_escape_string($_POST["name"]);
	$capital = $conn->real_escape_string($_POST["capital"]);
    $sql = "update country set name='$name', capital='$capital' where id='$id'";
//    echo $sql;
    if ($result = mysqli_query($conn, $sql)) {

        header("Location: /index.php");
    } else {
        echo "Ошибка: " . $conn->error;

    }


}
}?>
<html lang="en">
<head>
    <title>UPD country №<?php echo $_GET["id"];?></title>
    <link rel="stylesheet" href="style.css" type="text/css"/>
</head>
<body>
	
<?php
if ($id) {
    $sql = "select * from country where id=$id";
    if ($result = mysqli_query($conn, $sql)) {
        if ($result ->num_rows>0)
        foreach ($result as $row) {

            echo "<form  method='post'>
    <label for='name'>Название страны</label>
    <input type='text' name='name' id='name' value='{$row['name']}'>
	<label for='capital'>Столица</label>
	<input type='text' name='capital' id='capital' value='{$row['capital']}'>
    <input type='submit' value='Обновить'>
</form>";

        }
        else
            echo '404 not found';

    }
}
else{
    echo '404 not found';
}
    ?>


<a href="index.php">Все страны</a>

</body>
</html>
<?php $conn->close(); ?>
