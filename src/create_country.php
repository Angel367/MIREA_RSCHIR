<?php
if (isset($_POST["name"])) {
    $conn = new mysqli("db", "user", "password", "appDB");
    $name = $conn->real_escape_string($_POST["name"]);
    $capital = $conn->real_escape_string($_POST["capital"]);
    $sql = "INSERT IGNORE INTO country (name, capital) VALUES ('$name', '$capital')";

    if ($result = mysqli_query($conn, $sql)) {
        header("Location: /index.php");
    } else {
        echo "Ошибка: " . $conn->error;
    }
    $conn->close();
}
?>

<html lang="ru">
<head>
    <title>Create country</title>
    <link rel="stylesheet" href="style.css" type="text/css"/>
</head>
<body>
<form method="post">
    <label for="name">Название страны</label>
    <input type="text" name="name" id="name">
    <label for="capital">Столица</label>
    <input type="text" name="capital" id="capital">
    <input type="submit" value="Создать">
</form>

<a href="index.php">Все страны</a>
</body>
</html>
