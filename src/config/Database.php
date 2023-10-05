<?php

namespace config;

use Exception;
use mysqli;

class Database {
    private  $connection = null;
    public function __construct(){
        try {
            $this->connection = new mysqli("db", "user", "password", "appDB");
            if ( mysqli_connect_errno()) {
                echo "Could not connect to database.";
            }
        } catch (Exception $e) {
            echo  $e->getMessage();
        }
    }
    public function get_connection(): ?mysqli {
        return $this -> connection;
    }
}
