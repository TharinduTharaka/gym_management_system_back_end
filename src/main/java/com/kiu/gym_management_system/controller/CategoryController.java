package com.kiu.gym_management_system.controller;


import com.kiu.gym_management_system.model.maintenance.CategoryModel;
import com.kiu.gym_management_system.model.maintenance.MaintenanceModel;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.impl.CategoryManagerService;
import com.kiu.gym_management_system.service.impl.MaintenanceManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gym/maintenance/category")
@CrossOrigin(origins = "*")
public class CategoryController {


    @Autowired
    private MaintenanceManagerService maintenanceManagerService;

    @Autowired
    private CategoryManagerService categoryManagerService;

    @GetMapping("/get-all-category")
    public Response getAllCategory() {
        return categoryManagerService.getAllCategory();
    }

    @GetMapping("/get-category/{id}")
    public Response getCategory(@PathVariable int id) {
        return categoryManagerService.getCategory(id);
    }


    @GetMapping("/get-filter-category")
    public Response getFilterCategory(@RequestParam(value = "status") int status) {
        return categoryManagerService.getFilterCategory(status);
    }

    @PostMapping("/create-maintenance-category/{emp_id}")
    public Response createMaintenanceCategory(@PathVariable String emp_id,
                                              @RequestBody CategoryModel categoryModel) {
        return maintenanceManagerService.createMaintenanceCategory(emp_id, categoryModel);
    }

    @PutMapping("/update-category/{emp_id}/{id}")
    public Response editCategory(@PathVariable String emp_id, @PathVariable int id,
                                 @RequestBody CategoryModel categoryModel) {
        return categoryManagerService.editCategory(emp_id, categoryModel, id);
    }

    @PutMapping("/delete-category/{id}/{emp_id}")
    public Response deleteCategory(@PathVariable int id,
                                   @PathVariable String emp_id,
                                   @RequestParam(value = "status") int status) {
        return categoryManagerService.deleteCategory(status, id, emp_id);
    }

}
