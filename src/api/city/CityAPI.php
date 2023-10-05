<?php

namespace api\city;

use api\Api;
use api\country\Country;
require_once 'City.php';
require_once __DIR__.'/../Api.php';
require_once __DIR__ . '/../country/Country.php';
class CityAPI extends Api {
    public $apiName = 'city';
    protected function indexAction(): string {
        $cities = City::getAll($this -> db);
        if($cities){
            return $this->response($cities, 200);
        }
        return $this->response('Cities not found', 404);
    }
    protected function viewAction(): string {
        $id = $this->requestParams['id'] ?? null;
        $city = City::getByID($this->db, $id);
        if ($city) {
            return $this->response($city, 200);
        }
        return $this->response('City with id='.$id.' not found', 404);
    }
    public function createAction(): string {
        $name = $this -> db ->real_escape_string($this->requestParams['name']) ?? '';
        $population = $this -> db ->real_escape_string($this->requestParams['population']) ?? '';
        $country_id = $this -> db ->real_escape_string($this->requestParams['country_id']) ?? '';
        if($name && $population && $country_id){
            $city = new City($name, $population, $country_id);
            if( $city->saveNew($this -> db)){
                return $this->response($name.' saved.', 200);
            }
        }
        return $this->response('Saving error');
    }
    protected function updateAction(): string {
        $id = $this->requestParams['id'] ?? null;
        $city = City::getByID($this->db, $id);
        $name = $this->requestParams['name'] ?? $city['name'];
        $population = $this->requestParams['population'] ?? $city['population'];
        $country_id = Country::getByID($this->db, $this->requestParams['country_id'])['id'] ?? $city['country_id'];
        if ($city && City:: update($this -> db, $id,$name, $population, $country_id)) {
            return $this->response($name.' updated.', 200);
        }
        return $this->response("Updating error ");
    }
    protected function deleteAction() : string{
        $id = $this->requestParams['id'] ?? null;
        if(!City::getById($this -> db, $id)){
            return $this->response("City with id=$id not found", 404);
        }
        if(City::deleteById($this -> db, $id)){
            return $this->response("City with id=$id deleted", 200);
        }
        return $this->response("Deleting error");
    }
}