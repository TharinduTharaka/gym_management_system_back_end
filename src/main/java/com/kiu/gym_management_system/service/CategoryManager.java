package com.kiu.gym_management_system.service;

import com.kiu.gym_management_system.model.maintenance.CategoryModel;
import com.kiu.gym_management_system.model.maintenance.MaintenanceModel;
import com.kiu.gym_management_system.response.Response;

public interface CategoryManager {

    Response getAllCategory();

    Response getCategory(int id);

    Response getFilterCategory(int status);

    Response editCategory(String empID, CategoryModel categoryModel, int id);

    Response deleteCategory(int status, int id, String empID);


}
