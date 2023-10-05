<?php

require_once __DIR__ . '/CountryAPI.php';
try {
    $api = new \api\country\CountryAPI();
    echo $api->run();
} catch (Exception $e) {
    echo json_encode(Array('error' => $e->getMessage()));
}