<?php
require_once __DIR__ . '/CityAPI.php';
try {
    $api = new \api\city\CityAPI();
    echo $api->run();
} catch (Exception $e) {
    echo json_encode(Array('error' => $e->getMessage()));
}
