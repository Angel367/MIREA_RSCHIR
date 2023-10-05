<?php
$conn = new mysqli("db", "user", "password", "appDB", "3306");

if (isset($_POST["delete"]) && isset($_POST["countries"])) {
    $countries = $_POST["countries"];
    foreach ($countries as $country){
        $sql = "DELETE FROM country WHERE id = '$country'";

        if($result1 = mysqli_query($conn, $sql)){
//        echo "Данные успешно добавлены";
            header("Location: /index.php");
        } else{
            echo "Ошибка: " . $conn->error;
        }
    }

} elseif (isset($_POST["delete_one"])){
    $country = $_POST["delete_one"];
    $sql = "DELETE FROM country WHERE id = '$country'";
    if($result2 = mysqli_query($conn, $sql)){

        header("Location: /index.php");
    } else {
        echo "Ошибка: " . $conn->error;
    }
} else {
    header("Location: /index.php");
}

$conn->close();
?>