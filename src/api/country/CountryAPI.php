<?php

namespace api\country;
require_once __DIR__.'/../Api.php';
require_once 'Country.php';
use api\Api;
class CountryAPI extends Api{

    public $apiName = 'country';
    /**
     *  Метод GET
     *  Вывод списка всех записей
     *  api/country
     * @return string
     */
    protected function indexAction(): string{

        $categories = Country::getAll($this -> db);
        if($categories){
            return $this->response($categories, 200);
        }
        return $this->response('Categories not found', 404);
    }

    /**
     *  Метод GET
     *  Вывод записи по индексу
     *  api/country/id
     * @return string
     */
    protected function viewAction(): string{
        $id = $this->requestParams['id'] ?? null;
        $category = Country::getByID($this->db, $id);
        if ($category)
            return $this->response($category, 200);
        return $this->response('Country with id='.$id.' not found', 404);
    }

    public function createAction(): string{
        $name = $this -> db ->real_escape_string($this->requestParams['name'] ?? '');

        if($name){
            $country = new Country($name);
            if($country -> saveNew($this -> db)){
                return $this->response($name .' saved.', 200);
            }
        }
        return $this->response("Saving error");
    }

    protected function updateAction(): string{
        $id = $this->requestParams['id'] ?? null;
        $name = $this->requestParams['name'] ?? null;

        if (Country:: update($this -> db, $id, $name)){
                return $this->response("$name updated.", 200);
            }
        return $this->response("Updating error");
    }

    protected function deleteAction() : string{
        $id = $this->requestParams['id'] ?? null;
        if(!Country::getById($this -> db, $id)){
            return $this->response("Country with id=$id not found", 404);
        }
        if(Country::deleteById($this -> db, $id)){
            return $this->response('Country with id='.$id.' deleted.', 200);
        }
        return $this->response("Deleting error");
    }
}