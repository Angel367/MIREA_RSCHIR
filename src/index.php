<html lang="en">
<head>
    <title>Страны</title>
    <link rel="stylesheet" href="style.css" type="text/css"/>
</head>
<body>
<a href="create_country.php">Создать страну</a>

<h1>Таблица стран</h1>
<form action='delete_country.php' method='post'>
    <table>
        <tr>
            <th><input type="checkbox" id="select_all"></th>
            <th>№</th>
            <th>Название</th>
            <th>upd</th>
        </tr>
        <tr>
            <th><input type='submit' name="delete" value='Удалить выбранные'></th>
        </tr>
        <?php
        $mysqli = new mysqli("db", "user", "password", "appDB", "3306");
        $result = $mysqli->query("SELECT * FROM country ORDER BY id ");
        while ($row = $result->fetch_assoc()) {
            echo "<tr>
            <td><input type='checkbox' name='countries[]' value={$row['id']}></td>
            <td>{$row['id']}</td>
            <td>{$row['name']}</a></td>
            <td><a href='update_country.php?id={$row['id']}'>Обновить</a></td>
            <td><button type='submit' name='delete_one'  value={$row['id']}>Удалить</button></td>
            </tr>";
        }
        ?>
    </table>
</form>
<script>
    let select_all = document.getElementById('select_all');
    let checkboxes = document.getElementsByName('countries[]');
    console.log(select_all)
    select_all.addEventListener('change', function() {
        for (const checkbox of checkboxes) {
            checkbox.checked = select_all.checked
        }
    });
</script>
</body>
</html>
