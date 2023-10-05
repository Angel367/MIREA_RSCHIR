<?php

namespace api\country;

class Country{
    private  $name;

    public function __construct($name) {
        $this->name = $name;
    }
    public static function getAll($db): array {
        $result = $db->query("SELECT * FROM country");
        while($row = mysqli_fetch_assoc($result))
            $test[] = $row;
        return $test;
    }


    public static function getByID( $db, $id) {
        $result = $db->query("SELECT * FROM country where id = '$id'");
        while($row = mysqli_fetch_assoc($result))
            $test[] = $row;
        return $test[0];
    }
    public static function deleteById( $db, $id):bool{

        return $db->query("delete from country where id = '$id'");
    }
    public function saveNew($db): bool{
        return $db->query("INSERT IGNORE INTO country (name) VALUES ('$this->name')");
    }
    public static function update( $db,  $id, $name){
        return $db->query("UPDATE country set name = '$name' where id = '$id'");
    }
}