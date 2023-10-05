<?php
namespace api\city;
use mysqli;
class City {
    private $name;
    private $population;
    private $country_id;
    public function __construct($name, $population, $country_id) {
        $this->name = $name;
        $this->population = $population;
        $this->country_id = $country_id;
    }
    public static function getAll( $db): array {
        $result = $db->query("SELECT * FROM city");
        while($row = mysqli_fetch_assoc($result))
            $test[] = $row;
        return $test;
    }
    public static function getByID($db, $id) {
        $result = $db->query("SELECT * FROM city where id = '$id'");
        while($row = mysqli_fetch_assoc($result))
            $test[] = $row;
        return $test[0];
    }
    public static function deleteById($db, $id):bool{
        return $db->query("delete from city where id = '$id'");
    }
    public function saveNew(?mysqli $db): bool {
        return $db->query("INSERT IGNORE INTO city (name, population, country_id)
            VALUES ('$this->name', '$this->population', '$this->country_id')");
    }
    public static function update( $db, $id, $name, $population, $country_id){
        return $db->query("UPDATE city set name = '$name', population = '$population', 
                country_id = '$country_id' where id = '$id'");
    }
}