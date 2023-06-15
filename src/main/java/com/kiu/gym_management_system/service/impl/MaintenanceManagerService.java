package com.kiu.gym_management_system.service.impl;


import com.kiu.gym_management_system.entity.CategoryEntity;
import com.kiu.gym_management_system.entity.LoginEntity;
import com.kiu.gym_management_system.entity.MaintenanceEntity;
import com.kiu.gym_management_system.entity.ScheduleEntity;
import com.kiu.gym_management_system.model.maintenance.CategoryModel;
import com.kiu.gym_management_system.model.maintenance.MaintenanceModel;
import com.kiu.gym_management_system.model.schedule.ScheduleModel;
import com.kiu.gym_management_system.repository.CategoryRepository;
import com.kiu.gym_management_system.repository.LoginRepository;
import com.kiu.gym_management_system.repository.MaintenanceRepository;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.MaintenanceManager;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.DataFormatException;

@Service
public class MaintenanceManagerService implements MaintenanceManager {


    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    LoginRepository loginRepository;

    @Override
    public Response getAllMaintenance(String emp_id) {
        Response response = new Response();
        response.setCode(200);
        response.setData(maintenanceRepository.findAllByEmpCode(emp_id));
        response.setMsg("Get All Maintenance");
        return response;
    }

    @Override
    public Response getAllMaintenanceCategory() {
        Response response = new Response();
        response.setCode(200);
        response.setData(categoryRepository.findAll());
        response.setMsg("Get All Category");
        return response;
    }

    public Response getMaintenance(int id) {
        Response response = new Response();
        response.setCode(200);
        response.setData(maintenanceRepository.findById(id));
        response.setMsg("Get Maintenance");
        return response;
    }

    @Override
    public Response getFilterMaintenance(int status) {
        Response response = new Response();
        response.setCode(200);
        response.setData(maintenanceRepository.findByStatus(status));
        response.setMsg("Get admin filter Schedule Data");
        return response;

    }


    @Override
    public Response createMaintenanceCategory(String empID, CategoryModel categoryModel) {

        int employee_ID = Integer.parseInt(empID);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        CategoryEntity categoryEntity = new CategoryEntity();

        categoryEntity.setName(categoryModel.getName());
        categoryEntity.setCount(categoryModel.getCount());
        categoryEntity.setFrequency(categoryModel.getFrequency());
        categoryEntity.setStatus(1);
        categoryEntity.setDescription(categoryModel.getDescription());
        categoryEntity.setCreateBy(empID);
        categoryEntity.setCreateDate(new Date());

        List<CategoryEntity> categoryEntityArrayList = new ArrayList<>();

        categoryEntityArrayList.add(categoryEntity);
        categoryRepository.saveAll(categoryEntityArrayList);


        Response response = new Response();


        String title = categoryModel.getName();
        Optional<LoginEntity> loginEntityOptional = loginRepository.findById(employee_ID);
        if (loginEntityOptional.isPresent()) {
            LoginEntity loginEntity = loginEntityOptional.get();
            String toEmail = "tharindu.tharaka18@gmail.com";

            new Thread() {
                public void run() {
                    if (Objects.equals(loginEntity.getRole(), "admin")) {
                        sendEmailCategoryCreateByInstructor(toEmail, title, loginEntity.getFullName());
                    }

                }
            }.start();
        }
        createMaintenanceTaskForCategory(empID, categoryEntity.getId(), categoryModel.getCount(), categoryModel.getFrequency());
        response.setCode(201);
        response.setMsg("Create Maintenance Category Successful");
        response.setData(categoryRepository.findById(categoryEntity.getId()));
        return response;
    }

    public void createMaintenanceTaskForCategory(String empID, int category, int count, String frequency) {

        String dueDate = dueDateCalculate(count, frequency);

        MaintenanceEntity maintenanceEntity = new MaintenanceEntity();
        maintenanceEntity.setEmpCode(empID);
        maintenanceEntity.setDate(dueDate);
        maintenanceEntity.setCategory(category);
        maintenanceEntity.setStatus(1);
        maintenanceEntity.setCreateBy(empID);
        maintenanceEntity.setCreateDate(new Date());

        List<MaintenanceEntity> maintenanceEntityList = new ArrayList<>();

        maintenanceEntityList.add(maintenanceEntity);
        maintenanceRepository.saveAll(maintenanceEntityList);
    }

    public String dueDateCalculate(int count, String frequency) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date currentDate = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);


        if (frequency.equalsIgnoreCase("day")) {
            c.add(Calendar.DATE, count);

        } else if (frequency.equalsIgnoreCase("month")) {
            c.add(Calendar.MONTH, count);

        } else if (frequency.equalsIgnoreCase("year")) {
            c.add(Calendar.YEAR, count);

        } else if (frequency.equalsIgnoreCase("week")) {
            c.add(Calendar.YEAR, 7);

        }


        //same with c.add(Calendar.DAY_OF_MONTH, 1);
//        c.add(Calendar.HOUR, 1);
//        c.add(Calendar.MINUTE, 1);
//        c.add(Calendar.SECOND, 1);

        Date dueDate = c.getTime();

        String newDueDate = dateFormat.format(dueDate);

        return newDueDate;
    }

    @Override
    public Response createMaintenance(String empID, MaintenanceModel maintenanceModel) {

        int employee_ID = Integer.parseInt(empID);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        MaintenanceEntity maintenanceEntity = new MaintenanceEntity();

        maintenanceEntity.setEmpCode(empID);
        maintenanceEntity.setTitle(maintenanceModel.getTitle());
        maintenanceEntity.setDate(maintenanceModel.getDate());
        maintenanceEntity.setCategory(maintenanceModel.getCategory());
        maintenanceEntity.setStatus(maintenanceModel.getStatus());
        maintenanceEntity.setTaskID(maintenanceModel.getTaskID());
        maintenanceEntity.setTaskTitle(maintenanceModel.getTaskTitle());
        maintenanceEntity.setDescription(maintenanceModel.getDescription());
        maintenanceEntity.setCreateBy(empID);
        maintenanceEntity.setCreateDate(new Date());

        List<MaintenanceEntity> maintenanceEntityList = new ArrayList<>();

        maintenanceEntityList.add(maintenanceEntity);
        maintenanceRepository.saveAll(maintenanceEntityList);

        Response response = new Response();


        String title = maintenanceModel.getTitle();
        Optional<LoginEntity> loginEntityOptional = loginRepository.findById(employee_ID);
        if (loginEntityOptional.isPresent()) {
            LoginEntity loginEntity = loginEntityOptional.get();
            String toEmail = "tharindu.tharaka18@gmail.com";

            new Thread() {
                public void run() {
                    if (Objects.equals(loginEntity.getRole(), "admin")) {
                        sendEmailMaintenanceCreateByInstructor(toEmail, title, loginEntity.getFullName());
                    }

                }
            }.start();
        }

        response.setCode(201);
        response.setMsg("Create Maintenance Successful");
        response.setData(maintenanceRepository.findById(maintenanceEntity.getId()));
        return response;
    }


//    @Override
//    public Response createMaintenance(String empID, MaintenanceModel maintenanceModel) {
//
//        int employee_ID = Integer.parseInt(empID);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//
//        MaintenanceEntity maintenanceEntity = new MaintenanceEntity();
//
//        maintenanceEntity.setEmpCode(empID);
//        maintenanceEntity.setTitle(maintenanceModel.getTitle());
//        maintenanceEntity.setDate(maintenanceModel.getDate());
//        maintenanceEntity.setCategory(maintenanceModel.getCategory());
//        maintenanceEntity.setStatus(maintenanceModel.getStatus());
//        maintenanceEntity.setTaskID(maintenanceModel.getTaskID());
//        maintenanceEntity.setTaskTitle(maintenanceModel.getTaskTitle());
//        maintenanceEntity.setDescription(maintenanceModel.getDescription());
//        maintenanceEntity.setCreateBy(empID);
//        maintenanceEntity.setCreateDate(new Date());
//
//        List<MaintenanceEntity> maintenanceEntityList = new ArrayList<>();
//
//        maintenanceEntityList.add(maintenanceEntity);
//        maintenanceRepository.saveAll(maintenanceEntityList);
//
//        Response response = new Response();
//
//
//        String title = maintenanceModel.getTitle();
//        Optional<LoginEntity> loginEntityOptional = loginRepository.findById(employee_ID);
//        if (loginEntityOptional.isPresent()) {
//            LoginEntity loginEntity = loginEntityOptional.get();
//            String toEmail = "tharindu.tharaka18@gmail.com";
//
//            new Thread() {
//                public void run() {
//                    if (Objects.equals(loginEntity.getRole(), "admin")) {
//                        sendEmailMaintenanceCreateByInstructor(toEmail, title, loginEntity.getFullName());
//                    }
//
//                }
//            }.start();
//        }
//
//        response.setCode(201);
//        response.setMsg("Create Maintenance Successful");
//        response.setData(maintenanceRepository.findById(maintenanceEntity.getId()));
//        return response;
//    }


    @Override
    public Response editMaintenance(String empID, MaintenanceModel maintenanceModel, int id) {

        int employee_ID = Integer.parseInt(empID);
        Response response = new Response();
        Optional<MaintenanceEntity> maintenanceEntityOptional = maintenanceRepository.findById(id);

        if (maintenanceEntityOptional.isPresent()) {
            MaintenanceEntity maintenanceEntity = maintenanceEntityOptional.get();

            maintenanceEntity.setTitle(maintenanceModel.getTitle());
            maintenanceEntity.setDate(maintenanceModel.getDate());
            maintenanceEntity.setCategory(maintenanceModel.getCategory());
            maintenanceEntity.setStatus(maintenanceModel.getStatus());
            maintenanceEntity.setTaskID(maintenanceModel.getTaskID());
            maintenanceEntity.setTaskTitle(maintenanceModel.getTaskTitle());
            maintenanceEntity.setDescription(maintenanceModel.getDescription());
            maintenanceEntity.setUpdatedBy(empID);
            maintenanceEntity.setUpdatedDate(new Date());

            maintenanceRepository.save(maintenanceEntity);

            String title = maintenanceModel.getTitle();
            Optional<LoginEntity> loginEntityOptional = loginRepository.findById(employee_ID);
            if (loginEntityOptional.isPresent()) {
                LoginEntity loginEntity = loginEntityOptional.get();
                String toEmail = "tharindu.tharaka18@gmail.com";

                new Thread() {
                    public void run() {
                        if (Objects.equals(loginEntity.getRole(), "admin")) {
                            sendEmailMaintenanceEditByInstructor(toEmail, title, loginEntity.getFullName());
                        }

                    }
                }.start();
            }
            response.setCode(200);
            response.setMsg("Update Maintenance Successful");
//            response.setData(scheduleRepository.findById(obj.getId()));
            return response;
        } else {
            response.setCode(404);
            response.setMsg("Not Found");
            return response;
        }

    }

    @Override
    public Response completeMaintenance(int status, int id, String empID) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Response response = new Response();
        Optional<MaintenanceEntity> maintenanceEntityOptional = maintenanceRepository.findById(id);

        if (maintenanceEntityOptional.isPresent()) {
            MaintenanceEntity obj = maintenanceEntityOptional.get();
            Date currentDate = new Date();

            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(obj.getDate());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            if (currentDate.before(date)) {
                obj.setStatus(4);
            } else {
                obj.setStatus(status);
            }

            obj.setDeletedBy(empID);
            obj.setDeletedDate(new Date());



            Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(obj.getCategory());
            if (categoryEntityOptional.isPresent()) {
                CategoryEntity category = categoryEntityOptional.get();
                createMaintenanceTaskForCategory(empID, obj.getCategory(), category.getCount(), category.getFrequency());
            }

            maintenanceRepository.save(obj);
            response.setCode(200);
            response.setMsg("Complete Maintenance Successful");


            return response;
        } else {
            response.setCode(404);
            response.setMsg("Not Found");
            return response;
        }
//        }

    }

    @Override
    public Response deleteMaintenance(int status, int id, String empID) {

        int employee_ID = Integer.parseInt(empID);

        String employeeName, employeeRole;

//        List<LoginEntity> loginEntityList = loginRepository.findById(employee_ID);


//        for (LoginEntity loginEntity : loginEntityList) {
//            employeeName = loginEntity.getFullName();
//            employeeRole = loginEntity.getRole();
//        }


        Response response = new Response();
        Optional<MaintenanceEntity> maintenanceEntityOptional = maintenanceRepository.findById(id);

        if (maintenanceEntityOptional.isPresent()) {
            MaintenanceEntity obj = maintenanceEntityOptional.get();
            obj.setStatus(status);
            obj.setDeletedBy(empID);
            obj.setDeletedDate(new Date());
            maintenanceRepository.save(obj);
//            response.setData(scheduleRepository.findById(obj.getId()));

            String title = obj.getTitle();
            Optional<LoginEntity> loginEntityOptional = loginRepository.findById(employee_ID);

            if (loginEntityOptional.isPresent()) {
                LoginEntity loginEntity = loginEntityOptional.get();
                String toEmail = "tharindu.tharaka18@gmail.com";

                new Thread() {
                    public void run() {
                        if (Objects.equals(loginEntity.getRole(), "admin")) {
                            sendEmailMaintenanceDeleteByInstructor(toEmail, title, loginEntity.getFullName());
                        } else {
                            sendEmailMaintenanceDeleteByUser(toEmail, title, loginEntity.getFullName());
                        }

                    }
                }.start();
            }


            response.setCode(200);
            response.setMsg("Delete Reservation Successful");


            return response;
        } else {
            response.setCode(404);
            response.setMsg("Not Found");
            return response;
        }
//        }

    }


    public void sendEmailMaintenanceDeleteByUser(String toEmail, String title, String name) {
        String subject = "KIU GYM USER DELETE";
        String from = "kiugymportal@gmail.com";


        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(
                    "<!DOCTYPE html>\n" +
                            "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"utf-8\"> <!-- utf-8 works for most cases -->\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width\"> <!-- Forcing initial-scale shouldn't be necessary -->\n" +
                            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> <!-- Use the latest (edge) version of IE rendering engine -->\n" +
                            "    <meta name=\"x-apple-disable-message-reformatting\">  <!-- Disable auto-scale in iOS 10 Mail entirely -->\n" +
                            "    <title></title> <!-- The title tag shows in email notifications, like Android 4.4. -->\n" +
                            "\n" +
                            "    <link href=\"https://fonts.googleapis.com/css?family=Lato:300,400,700\" rel=\"stylesheet\">\n" +
                            "\n" +
                            "    <!-- CSS Reset : BEGIN -->\n" +
                            "    <style>\n" +
                            "\n" +
                            "        /* What it does: Remove spaces around the email design added by some email clients. */\n" +
                            "        /* Beware: It can remove the padding / margin and add a background color to the compose a reply window. */\n" +
                            "        html,\n" +
                            "body {\n" +
                            "    margin: 0 auto !important;\n" +
                            "    padding: 0 !important;\n" +
                            "    height: 100% !important;\n" +
                            "    width: 100% !important;\n" +
                            "    background: #f1f1f1;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Stops email clients resizing small text. */\n" +
                            "* {\n" +
                            "    -ms-text-size-adjust: 100%;\n" +
                            "    -webkit-text-size-adjust: 100%;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Centers email on Android 4.4 */\n" +
                            "div[style*=\"margin: 16px 0\"] {\n" +
                            "    margin: 0 !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Stops Outlook from adding extra spacing to tables. */\n" +
                            "table,\n" +
                            "td {\n" +
                            "    mso-table-lspace: 0pt !important;\n" +
                            "    mso-table-rspace: 0pt !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Fixes webkit padding issue. */\n" +
                            "table {\n" +
                            "    border-spacing: 0 !important;\n" +
                            "    border-collapse: collapse !important;\n" +
                            "    table-layout: fixed !important;\n" +
                            "    margin: 0 auto !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Uses a better rendering method when resizing images in IE. */\n" +
                            "img {\n" +
                            "    -ms-interpolation-mode:bicubic;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Windows 10 Mail from underlining links despite inline CSS. Styles for underlined links should be inline. */\n" +
                            "a {\n" +
                            "    text-decoration: none;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: A work-around for email clients meddling in triggered links. */\n" +
                            "*[x-apple-data-detectors],  /* iOS */\n" +
                            ".unstyle-auto-detected-links *,\n" +
                            ".aBn {\n" +
                            "    border-bottom: 0 !important;\n" +
                            "    cursor: default !important;\n" +
                            "    color: inherit !important;\n" +
                            "    text-decoration: none !important;\n" +
                            "    font-size: inherit !important;\n" +
                            "    font-family: inherit !important;\n" +
                            "    font-weight: inherit !important;\n" +
                            "    line-height: inherit !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Gmail from displaying a download button on large, non-linked images. */\n" +
                            ".a6S {\n" +
                            "    display: none !important;\n" +
                            "    opacity: 0.01 !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Gmail from changing the text color in conversation threads. */\n" +
                            ".im {\n" +
                            "    color: inherit !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* If the above doesn't work, add a .g-img class to any image in question. */\n" +
                            "img.g-img + div {\n" +
                            "    display: none !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Removes right gutter in Gmail iOS app: https://github.com/TedGoas/Cerberus/issues/89  */\n" +
                            "/* Create one of these media queries for each additional viewport size you'd like to fix */\n" +
                            "\n" +
                            "/* iPhone 4, 4S, 5, 5S, 5C, and 5SE */\n" +
                            "@media only screen and (min-device-width: 320px) and (max-device-width: 374px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 320px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "/* iPhone 6, 6S, 7, 8, and X */\n" +
                            "@media only screen and (min-device-width: 375px) and (max-device-width: 413px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 375px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "/* iPhone 6+, 7+, and 8+ */\n" +
                            "@media only screen and (min-device-width: 414px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 414px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "\n" +
                            "    </style>\n" +
                            "\n" +
                            "    <!-- CSS Reset : END -->\n" +
                            "\n" +
                            "    <!-- Progressive Enhancements : BEGIN -->\n" +
                            "    <style>\n" +
                            "\n" +
                            "\t    .primary{\n" +
                            "\tbackground: #30e3ca;\n" +
                            "}\n" +
                            ".bg_white{\n" +
                            "\tbackground: #ffffff;\n" +
                            "}\n" +
                            ".bg_light{\n" +
                            "\tbackground: #fafafa;\n" +
                            "}\n" +
                            ".bg_black{\n" +
                            "\tbackground: #000000;\n" +
                            "}\n" +
                            ".bg_dark{\n" +
                            "\tbackground: rgba(0,0,0,.8);\n" +
                            "}\n" +
                            ".email-section{\n" +
                            "\tpadding:2.5em;\n" +
                            "}\n" +
                            "\n" +
                            "/*BUTTON*/\n" +
                            ".btn{\n" +
                            "\tpadding: 10px 15px;\n" +
                            "\tdisplay: inline-block;\n" +
                            "}\n" +
                            ".btn.btn-primary{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: #30e3ca;\n" +
                            "\tcolor: #ffffff;\n" +
                            "}\n" +
                            ".btn.btn-white{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: #ffffff;\n" +
                            "\tcolor: #000000;\n" +
                            "}\n" +
                            ".btn.btn-white-outline{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: transparent;\n" +
                            "\tborder: 1px solid #fff;\n" +
                            "\tcolor: #fff;\n" +
                            "}\n" +
                            ".btn.btn-black-outline{\n" +
                            "\tborder-radius: 0px;\n" +
                            "\tbackground: transparent;\n" +
                            "\tborder: 2px solid #000;\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-weight: 700;\n" +
                            "}\n" +
                            "\n" +
                            "h1,h2,h3,h4,h5,h6{\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "\tcolor: #000000;\n" +
                            "\tmargin-top: 0;\n" +
                            "\tfont-weight: 400;\n" +
                            "}\n" +
                            "\n" +
                            "body{\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "\tfont-weight: 400;\n" +
                            "\tfont-size: 15px;\n" +
                            "\tline-height: 1.8;\n" +
                            "\tcolor: rgba(0,0,0,.4);\n" +
                            "}\n" +
                            "\n" +
                            "a{\n" +
                            "\tcolor: #30e3ca;\n" +
                            "}\n" +
                            "\n" +
                            "table{\n" +
                            "}\n" +
                            "/*LOGO*/\n" +
                            "\n" +
                            ".logo h1{\n" +
                            "\tmargin: 0;\n" +
                            "}\n" +
                            ".logo h1 a{\n" +
                            "\tcolor: #30e3ca;\n" +
                            "\tfont-size: 24px;\n" +
                            "\tfont-weight: 700;\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "}\n" +
                            "\n" +
                            "/*HERO*/\n" +
                            ".hero{\n" +
                            "\tposition: relative;\n" +
                            "\tz-index: 0;\n" +
                            "}\n" +
                            "\n" +
                            ".hero .text{\n" +
                            "\tcolor: rgba(0,0,0,.3);\n" +
                            "}\n" +
                            ".hero .text h2{\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-size: 40px;\n" +
                            "\tmargin-bottom: 0;\n" +
                            "\tfont-weight: 400;\n" +
                            "\tline-height: 1.4;\n" +
                            "}\n" +
                            ".hero .text h3{\n" +
                            "\tfont-size: 24px;\n" +
                            "\tfont-weight: 300;\n" +
                            "}\n" +
                            ".hero .text h2 span{\n" +
                            "\tfont-weight: 600;\n" +
                            "\tcolor: #30e3ca;\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "/*HEADING SECTION*/\n" +
                            ".heading-section{\n" +
                            "}\n" +
                            ".heading-section h2{\n" +
                            "\tcolor: #000000;\n" +
                            "\tfont-size: 28px;\n" +
                            "\tmargin-top: 0;\n" +
                            "\tline-height: 1.4;\n" +
                            "\tfont-weight: 400;\n" +
                            "}\n" +
                            ".heading-section .subheading{\n" +
                            "\tmargin-bottom: 20px !important;\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tfont-size: 13px;\n" +
                            "\ttext-transform: uppercase;\n" +
                            "\tletter-spacing: 2px;\n" +
                            "\tcolor: rgba(0,0,0,.4);\n" +
                            "\tposition: relative;\n" +
                            "}\n" +
                            ".heading-section .subheading::after{\n" +
                            "\tposition: absolute;\n" +
                            "\tleft: 0;\n" +
                            "\tright: 0;\n" +
                            "\tbottom: -10px;\n" +
                            "\tcontent: '';\n" +
                            "\twidth: 100%;\n" +
                            "\theight: 2px;\n" +
                            "\tbackground: #30e3ca;\n" +
                            "\tmargin: 0 auto;\n" +
                            "}\n" +
                            "\n" +
                            ".heading-section-white{\n" +
                            "\tcolor: rgba(255,255,255,.8);\n" +
                            "}\n" +
                            ".heading-section-white h2{\n" +
                            "\tfont-family: \n" +
                            "\tline-height: 1;\n" +
                            "\tpadding-bottom: 0;\n" +
                            "}\n" +
                            ".heading-section-white h2{\n" +
                            "\tcolor: #ffffff;\n" +
                            "}\n" +
                            ".heading-section-white .subheading{\n" +
                            "\tmargin-bottom: 0;\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tfont-size: 13px;\n" +
                            "\ttext-transform: uppercase;\n" +
                            "\tletter-spacing: 2px;\n" +
                            "\tcolor: rgba(255,255,255,.4);\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "ul.social{\n" +
                            "\tpadding: 0;\n" +
                            "}\n" +
                            "ul.social li{\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tmargin-right: 10px;\n" +
                            "}\n" +
                            "\n" +
                            "/*FOOTER*/\n" +
                            "\n" +
                            ".footer{\n" +
                            "\tborder-top: 1px solid rgba(0,0,0,.05);\n" +
                            "\tcolor: rgba(0,0,0,.5);\n" +
                            "}\n" +
                            ".footer .heading{\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-size: 20px;\n" +
                            "}\n" +
                            ".footer ul{\n" +
                            "\tmargin: 0;\n" +
                            "\tpadding: 0;\n" +
                            "}\n" +
                            ".footer ul li{\n" +
                            "\tlist-style: none;\n" +
                            "\tmargin-bottom: 10px;\n" +
                            "}\n" +
                            ".footer ul li a{\n" +
                            "\tcolor: rgba(0,0,0,1);\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "@media screen and (max-width: 500px) {\n" +
                            "\n" +
                            "\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "    </style>\n" +
                            "\n" +
                            "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                            "</head>\n" +
                            "\n" +
                            "<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly; background-color: #f1f1f1;\">\n" +
                            "\t<center style=\"width: 100%; background-color: #f1f1f1;\">\n" +
                            "    <div style=\"display: none; font-size: 1px;max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden; mso-hide: all; font-family: sans-serif;\">\n" +
                            "      &zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;\n" +
                            "    </div>\n" +
                            "    <div style=\"max-width: 600px; margin: 0 auto;\" class=\"email-container\">\n" +
                            "    \t<!-- BEGIN BODY -->\n" +
                            "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                            "      \t<tr>\n" +
                            "          <td valign=\"top\" class=\"bg_white\" style=\"padding: 1em 2.5em 0 2.5em;\">\n" +
                            "          \t<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                            "          \t\t<tr>\n" +
                            "          \t\t\t<td class=\"logo\" style=\"text-align: center;\">\n" +
                            "\t\t\t            <h1><a href=\"#\">KIU GYM</a></h1>\n" +
                            "\t\t\t          </td>\n" +
                            "          \t\t</tr>\n" +
                            "          \t</table>\n" +
                            "          </td>\n" +
                            "\t      </tr><!-- end tr -->\n" +
                            "\t\t\t\t<tr>\n" +
                            "          <td valign=\"middle\" class=\"hero bg_white\" style=\"padding: 2em 0 4em 0;\">\n" +
                            "            <table>\n" +
                            "            \t<tr>\n" +
                            "            \t\t<td>\n" +
                            "            \t\t\t<div class=\"text\" style=\"padding: 0 2.5em; text-align: center;\">\n" +
                            "            \t\t\t\t<h2>Hi,Schedule Has Been</h2>\n" +
                            "            \t\t\t\t<h2 style=\"color: red\">Deleted</h2>\n" +
                            "            \t\t\t\t<h3>Schedule Title</h3>\n" +
                            "\t\t\t\t\t\t\t<h3 style=\"color: blue\">" + title + "</h3>\n" +
                            "            \t\t\t\t<h3>Deleted By</h3>" + name + "</h3>\n" +
                            "\t\t\t\t\t\t\t<h3>Please Review It !</h3>\n" +
//                            "            \t\t\t\t<p><a href=\"http://13.232.138.190:8087/apps/myTask\" class=\"btn btn-primary\">Go To Portal</a></p>\n" +
                            "            \t\t\t</div>\n" +
                            "            \t\t</td>\n" +
                            "            \t</tr>\n" +
                            "            </table>\n" +
                            "          </td>\n" +
                            "\t      </tr><!-- end tr -->\n" +
                            "      <!-- 1 Column Text + Button : END -->\n" +
                            "      </table>\n" +
                            "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                            "    \n" +
                            "        <tr>\n" +
                            "          <td class=\"bg_light\" style=\"text-align: center;\">\n" +
                            "          \t<p>This ia an auto generated email <a href=\"#\" style=\"color: rgba(0,0,0,.8);\"><i class=\"fa fa-heart\" style=\"font-size:20px;color:red\"></i> By KIU GYM</a></p>\n" +
                            "          </td>\n" +
                            "        </tr>\n" +
                            "      </table>\n" +
                            "\n" +
                            "    </div>\n" +
                            "  </center>\n" +
                            "</body>\n" +
                            "</html>",
                    true);
            emailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmailMaintenanceDeleteByInstructor(String toEmail, String title, String name) {
        String subject = "KIU GYM INSTRUCTOR DELETE";
        String from = "kiugymportal@gmail.com";


        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(
                    "<!DOCTYPE html>\n" +
                            "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"utf-8\"> <!-- utf-8 works for most cases -->\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width\"> <!-- Forcing initial-scale shouldn't be necessary -->\n" +
                            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> <!-- Use the latest (edge) version of IE rendering engine -->\n" +
                            "    <meta name=\"x-apple-disable-message-reformatting\">  <!-- Disable auto-scale in iOS 10 Mail entirely -->\n" +
                            "    <title></title> <!-- The title tag shows in email notifications, like Android 4.4. -->\n" +
                            "\n" +
                            "    <link href=\"https://fonts.googleapis.com/css?family=Lato:300,400,700\" rel=\"stylesheet\">\n" +
                            "\n" +
                            "    <!-- CSS Reset : BEGIN -->\n" +
                            "    <style>\n" +
                            "\n" +
                            "        /* What it does: Remove spaces around the email design added by some email clients. */\n" +
                            "        /* Beware: It can remove the padding / margin and add a background color to the compose a reply window. */\n" +
                            "        html,\n" +
                            "body {\n" +
                            "    margin: 0 auto !important;\n" +
                            "    padding: 0 !important;\n" +
                            "    height: 100% !important;\n" +
                            "    width: 100% !important;\n" +
                            "    background: #f1f1f1;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Stops email clients resizing small text. */\n" +
                            "* {\n" +
                            "    -ms-text-size-adjust: 100%;\n" +
                            "    -webkit-text-size-adjust: 100%;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Centers email on Android 4.4 */\n" +
                            "div[style*=\"margin: 16px 0\"] {\n" +
                            "    margin: 0 !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Stops Outlook from adding extra spacing to tables. */\n" +
                            "table,\n" +
                            "td {\n" +
                            "    mso-table-lspace: 0pt !important;\n" +
                            "    mso-table-rspace: 0pt !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Fixes webkit padding issue. */\n" +
                            "table {\n" +
                            "    border-spacing: 0 !important;\n" +
                            "    border-collapse: collapse !important;\n" +
                            "    table-layout: fixed !important;\n" +
                            "    margin: 0 auto !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Uses a better rendering method when resizing images in IE. */\n" +
                            "img {\n" +
                            "    -ms-interpolation-mode:bicubic;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Windows 10 Mail from underlining links despite inline CSS. Styles for underlined links should be inline. */\n" +
                            "a {\n" +
                            "    text-decoration: none;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: A work-around for email clients meddling in triggered links. */\n" +
                            "*[x-apple-data-detectors],  /* iOS */\n" +
                            ".unstyle-auto-detected-links *,\n" +
                            ".aBn {\n" +
                            "    border-bottom: 0 !important;\n" +
                            "    cursor: default !important;\n" +
                            "    color: inherit !important;\n" +
                            "    text-decoration: none !important;\n" +
                            "    font-size: inherit !important;\n" +
                            "    font-family: inherit !important;\n" +
                            "    font-weight: inherit !important;\n" +
                            "    line-height: inherit !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Gmail from displaying a download button on large, non-linked images. */\n" +
                            ".a6S {\n" +
                            "    display: none !important;\n" +
                            "    opacity: 0.01 !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Gmail from changing the text color in conversation threads. */\n" +
                            ".im {\n" +
                            "    color: inherit !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* If the above doesn't work, add a .g-img class to any image in question. */\n" +
                            "img.g-img + div {\n" +
                            "    display: none !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Removes right gutter in Gmail iOS app: https://github.com/TedGoas/Cerberus/issues/89  */\n" +
                            "/* Create one of these media queries for each additional viewport size you'd like to fix */\n" +
                            "\n" +
                            "/* iPhone 4, 4S, 5, 5S, 5C, and 5SE */\n" +
                            "@media only screen and (min-device-width: 320px) and (max-device-width: 374px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 320px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "/* iPhone 6, 6S, 7, 8, and X */\n" +
                            "@media only screen and (min-device-width: 375px) and (max-device-width: 413px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 375px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "/* iPhone 6+, 7+, and 8+ */\n" +
                            "@media only screen and (min-device-width: 414px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 414px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "\n" +
                            "    </style>\n" +
                            "\n" +
                            "    <!-- CSS Reset : END -->\n" +
                            "\n" +
                            "    <!-- Progressive Enhancements : BEGIN -->\n" +
                            "    <style>\n" +
                            "\n" +
                            "\t    .primary{\n" +
                            "\tbackground: #30e3ca;\n" +
                            "}\n" +
                            ".bg_white{\n" +
                            "\tbackground: #ffffff;\n" +
                            "}\n" +
                            ".bg_light{\n" +
                            "\tbackground: #fafafa;\n" +
                            "}\n" +
                            ".bg_black{\n" +
                            "\tbackground: #000000;\n" +
                            "}\n" +
                            ".bg_dark{\n" +
                            "\tbackground: rgba(0,0,0,.8);\n" +
                            "}\n" +
                            ".email-section{\n" +
                            "\tpadding:2.5em;\n" +
                            "}\n" +
                            "\n" +
                            "/*BUTTON*/\n" +
                            ".btn{\n" +
                            "\tpadding: 10px 15px;\n" +
                            "\tdisplay: inline-block;\n" +
                            "}\n" +
                            ".btn.btn-primary{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: #30e3ca;\n" +
                            "\tcolor: #ffffff;\n" +
                            "}\n" +
                            ".btn.btn-white{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: #ffffff;\n" +
                            "\tcolor: #000000;\n" +
                            "}\n" +
                            ".btn.btn-white-outline{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: transparent;\n" +
                            "\tborder: 1px solid #fff;\n" +
                            "\tcolor: #fff;\n" +
                            "}\n" +
                            ".btn.btn-black-outline{\n" +
                            "\tborder-radius: 0px;\n" +
                            "\tbackground: transparent;\n" +
                            "\tborder: 2px solid #000;\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-weight: 700;\n" +
                            "}\n" +
                            "\n" +
                            "h1,h2,h3,h4,h5,h6{\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "\tcolor: #000000;\n" +
                            "\tmargin-top: 0;\n" +
                            "\tfont-weight: 400;\n" +
                            "}\n" +
                            "\n" +
                            "body{\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "\tfont-weight: 400;\n" +
                            "\tfont-size: 15px;\n" +
                            "\tline-height: 1.8;\n" +
                            "\tcolor: rgba(0,0,0,.4);\n" +
                            "}\n" +
                            "\n" +
                            "a{\n" +
                            "\tcolor: #30e3ca;\n" +
                            "}\n" +
                            "\n" +
                            "table{\n" +
                            "}\n" +
                            "/*LOGO*/\n" +
                            "\n" +
                            ".logo h1{\n" +
                            "\tmargin: 0;\n" +
                            "}\n" +
                            ".logo h1 a{\n" +
                            "\tcolor: #30e3ca;\n" +
                            "\tfont-size: 24px;\n" +
                            "\tfont-weight: 700;\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "}\n" +
                            "\n" +
                            "/*HERO*/\n" +
                            ".hero{\n" +
                            "\tposition: relative;\n" +
                            "\tz-index: 0;\n" +
                            "}\n" +
                            "\n" +
                            ".hero .text{\n" +
                            "\tcolor: rgba(0,0,0,.3);\n" +
                            "}\n" +
                            ".hero .text h2{\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-size: 40px;\n" +
                            "\tmargin-bottom: 0;\n" +
                            "\tfont-weight: 400;\n" +
                            "\tline-height: 1.4;\n" +
                            "}\n" +
                            ".hero .text h3{\n" +
                            "\tfont-size: 24px;\n" +
                            "\tfont-weight: 300;\n" +
                            "}\n" +
                            ".hero .text h2 span{\n" +
                            "\tfont-weight: 600;\n" +
                            "\tcolor: #30e3ca;\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "/*HEADING SECTION*/\n" +
                            ".heading-section{\n" +
                            "}\n" +
                            ".heading-section h2{\n" +
                            "\tcolor: #000000;\n" +
                            "\tfont-size: 28px;\n" +
                            "\tmargin-top: 0;\n" +
                            "\tline-height: 1.4;\n" +
                            "\tfont-weight: 400;\n" +
                            "}\n" +
                            ".heading-section .subheading{\n" +
                            "\tmargin-bottom: 20px !important;\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tfont-size: 13px;\n" +
                            "\ttext-transform: uppercase;\n" +
                            "\tletter-spacing: 2px;\n" +
                            "\tcolor: rgba(0,0,0,.4);\n" +
                            "\tposition: relative;\n" +
                            "}\n" +
                            ".heading-section .subheading::after{\n" +
                            "\tposition: absolute;\n" +
                            "\tleft: 0;\n" +
                            "\tright: 0;\n" +
                            "\tbottom: -10px;\n" +
                            "\tcontent: '';\n" +
                            "\twidth: 100%;\n" +
                            "\theight: 2px;\n" +
                            "\tbackground: #30e3ca;\n" +
                            "\tmargin: 0 auto;\n" +
                            "}\n" +
                            "\n" +
                            ".heading-section-white{\n" +
                            "\tcolor: rgba(255,255,255,.8);\n" +
                            "}\n" +
                            ".heading-section-white h2{\n" +
                            "\tfont-family: \n" +
                            "\tline-height: 1;\n" +
                            "\tpadding-bottom: 0;\n" +
                            "}\n" +
                            ".heading-section-white h2{\n" +
                            "\tcolor: #ffffff;\n" +
                            "}\n" +
                            ".heading-section-white .subheading{\n" +
                            "\tmargin-bottom: 0;\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tfont-size: 13px;\n" +
                            "\ttext-transform: uppercase;\n" +
                            "\tletter-spacing: 2px;\n" +
                            "\tcolor: rgba(255,255,255,.4);\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "ul.social{\n" +
                            "\tpadding: 0;\n" +
                            "}\n" +
                            "ul.social li{\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tmargin-right: 10px;\n" +
                            "}\n" +
                            "\n" +
                            "/*FOOTER*/\n" +
                            "\n" +
                            ".footer{\n" +
                            "\tborder-top: 1px solid rgba(0,0,0,.05);\n" +
                            "\tcolor: rgba(0,0,0,.5);\n" +
                            "}\n" +
                            ".footer .heading{\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-size: 20px;\n" +
                            "}\n" +
                            ".footer ul{\n" +
                            "\tmargin: 0;\n" +
                            "\tpadding: 0;\n" +
                            "}\n" +
                            ".footer ul li{\n" +
                            "\tlist-style: none;\n" +
                            "\tmargin-bottom: 10px;\n" +
                            "}\n" +
                            ".footer ul li a{\n" +
                            "\tcolor: rgba(0,0,0,1);\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "@media screen and (max-width: 500px) {\n" +
                            "\n" +
                            "\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "    </style>\n" +
                            "\n" +
                            "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                            "</head>\n" +
                            "\n" +
                            "<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly; background-color: #f1f1f1;\">\n" +
                            "\t<center style=\"width: 100%; background-color: #f1f1f1;\">\n" +
                            "    <div style=\"display: none; font-size: 1px;max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden; mso-hide: all; font-family: sans-serif;\">\n" +
                            "      &zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;\n" +
                            "    </div>\n" +
                            "    <div style=\"max-width: 600px; margin: 0 auto;\" class=\"email-container\">\n" +
                            "    \t<!-- BEGIN BODY -->\n" +
                            "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                            "      \t<tr>\n" +
                            "          <td valign=\"top\" class=\"bg_white\" style=\"padding: 1em 2.5em 0 2.5em;\">\n" +
                            "          \t<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                            "          \t\t<tr>\n" +
                            "          \t\t\t<td class=\"logo\" style=\"text-align: center;\">\n" +
                            "\t\t\t            <h1><a href=\"#\">KIU GYM</a></h1>\n" +
                            "\t\t\t          </td>\n" +
                            "          \t\t</tr>\n" +
                            "          \t</table>\n" +
                            "          </td>\n" +
                            "\t      </tr><!-- end tr -->\n" +
                            "\t\t\t\t<tr>\n" +
                            "          <td valign=\"middle\" class=\"hero bg_white\" style=\"padding: 2em 0 4em 0;\">\n" +
                            "            <table>\n" +
                            "            \t<tr>\n" +
                            "            \t\t<td>\n" +
                            "            \t\t\t<div class=\"text\" style=\"padding: 0 2.5em; text-align: center;\">\n" +
                            "            \t\t\t\t<h2>Hi,Schedule Has Been</h2>\n" +
                            "            \t\t\t\t<h2 style=\"color: red\">Deleted</h2>\n" +
                            "            \t\t\t\t<h3>Schedule Title</h3>\n" +
                            "\t\t\t\t\t\t\t<h3 style=\"color: blue\">" + title + "</h3>\n" +
                            "            \t\t\t\t<h3>Deleted By</h3>" + name + "(Instructor)" + "</h3>\n" +
                            "\t\t\t\t\t\t\t<h3>Please Review It !</h3>\n" +
//                            "            \t\t\t\t<p><a href=\"http://13.232.138.190:8087/apps/myTask\" class=\"btn btn-primary\">Go To Portal</a></p>\n" +
                            "            \t\t\t</div>\n" +
                            "            \t\t</td>\n" +
                            "            \t</tr>\n" +
                            "            </table>\n" +
                            "          </td>\n" +
                            "\t      </tr><!-- end tr -->\n" +
                            "      <!-- 1 Column Text + Button : END -->\n" +
                            "      </table>\n" +
                            "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                            "    \n" +
                            "        <tr>\n" +
                            "          <td class=\"bg_light\" style=\"text-align: center;\">\n" +
                            "          \t<p>This ia an auto generated email <a href=\"#\" style=\"color: rgba(0,0,0,.8);\"><i class=\"fa fa-heart\" style=\"font-size:20px;color:red\"></i> By KIU GYM</a></p>\n" +
                            "          </td>\n" +
                            "        </tr>\n" +
                            "      </table>\n" +
                            "\n" +
                            "    </div>\n" +
                            "  </center>\n" +
                            "</body>\n" +
                            "</html>",
                    true);
            emailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendEmailMaintenanceCreateByInstructor(String toEmail, String title, String name) {
        String subject = "KIU GYM CREATE MAINTENANCE";
        String from = "kiugymportal@gmail.com";


        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(
                    "<!DOCTYPE html>\n" +
                            "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"utf-8\"> <!-- utf-8 works for most cases -->\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width\"> <!-- Forcing initial-scale shouldn't be necessary -->\n" +
                            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> <!-- Use the latest (edge) version of IE rendering engine -->\n" +
                            "    <meta name=\"x-apple-disable-message-reformatting\">  <!-- Disable auto-scale in iOS 10 Mail entirely -->\n" +
                            "    <title></title> <!-- The title tag shows in email notifications, like Android 4.4. -->\n" +
                            "\n" +
                            "    <link href=\"https://fonts.googleapis.com/css?family=Lato:300,400,700\" rel=\"stylesheet\">\n" +
                            "\n" +
                            "    <!-- CSS Reset : BEGIN -->\n" +
                            "    <style>\n" +
                            "\n" +
                            "        /* What it does: Remove spaces around the email design added by some email clients. */\n" +
                            "        /* Beware: It can remove the padding / margin and add a background color to the compose a reply window. */\n" +
                            "        html,\n" +
                            "body {\n" +
                            "    margin: 0 auto !important;\n" +
                            "    padding: 0 !important;\n" +
                            "    height: 100% !important;\n" +
                            "    width: 100% !important;\n" +
                            "    background: #f1f1f1;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Stops email clients resizing small text. */\n" +
                            "* {\n" +
                            "    -ms-text-size-adjust: 100%;\n" +
                            "    -webkit-text-size-adjust: 100%;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Centers email on Android 4.4 */\n" +
                            "div[style*=\"margin: 16px 0\"] {\n" +
                            "    margin: 0 !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Stops Outlook from adding extra spacing to tables. */\n" +
                            "table,\n" +
                            "td {\n" +
                            "    mso-table-lspace: 0pt !important;\n" +
                            "    mso-table-rspace: 0pt !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Fixes webkit padding issue. */\n" +
                            "table {\n" +
                            "    border-spacing: 0 !important;\n" +
                            "    border-collapse: collapse !important;\n" +
                            "    table-layout: fixed !important;\n" +
                            "    margin: 0 auto !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Uses a better rendering method when resizing images in IE. */\n" +
                            "img {\n" +
                            "    -ms-interpolation-mode:bicubic;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Windows 10 Mail from underlining links despite inline CSS. Styles for underlined links should be inline. */\n" +
                            "a {\n" +
                            "    text-decoration: none;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: A work-around for email clients meddling in triggered links. */\n" +
                            "*[x-apple-data-detectors],  /* iOS */\n" +
                            ".unstyle-auto-detected-links *,\n" +
                            ".aBn {\n" +
                            "    border-bottom: 0 !important;\n" +
                            "    cursor: default !important;\n" +
                            "    color: inherit !important;\n" +
                            "    text-decoration: none !important;\n" +
                            "    font-size: inherit !important;\n" +
                            "    font-family: inherit !important;\n" +
                            "    font-weight: inherit !important;\n" +
                            "    line-height: inherit !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Gmail from displaying a download button on large, non-linked images. */\n" +
                            ".a6S {\n" +
                            "    display: none !important;\n" +
                            "    opacity: 0.01 !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Gmail from changing the text color in conversation threads. */\n" +
                            ".im {\n" +
                            "    color: inherit !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* If the above doesn't work, add a .g-img class to any image in question. */\n" +
                            "img.g-img + div {\n" +
                            "    display: none !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Removes right gutter in Gmail iOS app: https://github.com/TedGoas/Cerberus/issues/89  */\n" +
                            "/* Create one of these media queries for each additional viewport size you'd like to fix */\n" +
                            "\n" +
                            "/* iPhone 4, 4S, 5, 5S, 5C, and 5SE */\n" +
                            "@media only screen and (min-device-width: 320px) and (max-device-width: 374px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 320px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "/* iPhone 6, 6S, 7, 8, and X */\n" +
                            "@media only screen and (min-device-width: 375px) and (max-device-width: 413px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 375px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "/* iPhone 6+, 7+, and 8+ */\n" +
                            "@media only screen and (min-device-width: 414px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 414px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "\n" +
                            "    </style>\n" +
                            "\n" +
                            "    <!-- CSS Reset : END -->\n" +
                            "\n" +
                            "    <!-- Progressive Enhancements : BEGIN -->\n" +
                            "    <style>\n" +
                            "\n" +
                            "\t    .primary{\n" +
                            "\tbackground: #30e3ca;\n" +
                            "}\n" +
                            ".bg_white{\n" +
                            "\tbackground: #ffffff;\n" +
                            "}\n" +
                            ".bg_light{\n" +
                            "\tbackground: #fafafa;\n" +
                            "}\n" +
                            ".bg_black{\n" +
                            "\tbackground: #000000;\n" +
                            "}\n" +
                            ".bg_dark{\n" +
                            "\tbackground: rgba(0,0,0,.8);\n" +
                            "}\n" +
                            ".email-section{\n" +
                            "\tpadding:2.5em;\n" +
                            "}\n" +
                            "\n" +
                            "/*BUTTON*/\n" +
                            ".btn{\n" +
                            "\tpadding: 10px 15px;\n" +
                            "\tdisplay: inline-block;\n" +
                            "}\n" +
                            ".btn.btn-primary{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: #30e3ca;\n" +
                            "\tcolor: #ffffff;\n" +
                            "}\n" +
                            ".btn.btn-white{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: #ffffff;\n" +
                            "\tcolor: #000000;\n" +
                            "}\n" +
                            ".btn.btn-white-outline{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: transparent;\n" +
                            "\tborder: 1px solid #fff;\n" +
                            "\tcolor: #fff;\n" +
                            "}\n" +
                            ".btn.btn-black-outline{\n" +
                            "\tborder-radius: 0px;\n" +
                            "\tbackground: transparent;\n" +
                            "\tborder: 2px solid #000;\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-weight: 700;\n" +
                            "}\n" +
                            "\n" +
                            "h1,h2,h3,h4,h5,h6{\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "\tcolor: #000000;\n" +
                            "\tmargin-top: 0;\n" +
                            "\tfont-weight: 400;\n" +
                            "}\n" +
                            "\n" +
                            "body{\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "\tfont-weight: 400;\n" +
                            "\tfont-size: 15px;\n" +
                            "\tline-height: 1.8;\n" +
                            "\tcolor: rgba(0,0,0,.4);\n" +
                            "}\n" +
                            "\n" +
                            "a{\n" +
                            "\tcolor: #30e3ca;\n" +
                            "}\n" +
                            "\n" +
                            "table{\n" +
                            "}\n" +
                            "/*LOGO*/\n" +
                            "\n" +
                            ".logo h1{\n" +
                            "\tmargin: 0;\n" +
                            "}\n" +
                            ".logo h1 a{\n" +
                            "\tcolor: #30e3ca;\n" +
                            "\tfont-size: 24px;\n" +
                            "\tfont-weight: 700;\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "}\n" +
                            "\n" +
                            "/*HERO*/\n" +
                            ".hero{\n" +
                            "\tposition: relative;\n" +
                            "\tz-index: 0;\n" +
                            "}\n" +
                            "\n" +
                            ".hero .text{\n" +
                            "\tcolor: rgba(0,0,0,.3);\n" +
                            "}\n" +
                            ".hero .text h2{\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-size: 40px;\n" +
                            "\tmargin-bottom: 0;\n" +
                            "\tfont-weight: 400;\n" +
                            "\tline-height: 1.4;\n" +
                            "}\n" +
                            ".hero .text h3{\n" +
                            "\tfont-size: 24px;\n" +
                            "\tfont-weight: 300;\n" +
                            "}\n" +
                            ".hero .text h2 span{\n" +
                            "\tfont-weight: 600;\n" +
                            "\tcolor: #30e3ca;\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "/*HEADING SECTION*/\n" +
                            ".heading-section{\n" +
                            "}\n" +
                            ".heading-section h2{\n" +
                            "\tcolor: #000000;\n" +
                            "\tfont-size: 28px;\n" +
                            "\tmargin-top: 0;\n" +
                            "\tline-height: 1.4;\n" +
                            "\tfont-weight: 400;\n" +
                            "}\n" +
                            ".heading-section .subheading{\n" +
                            "\tmargin-bottom: 20px !important;\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tfont-size: 13px;\n" +
                            "\ttext-transform: uppercase;\n" +
                            "\tletter-spacing: 2px;\n" +
                            "\tcolor: rgba(0,0,0,.4);\n" +
                            "\tposition: relative;\n" +
                            "}\n" +
                            ".heading-section .subheading::after{\n" +
                            "\tposition: absolute;\n" +
                            "\tleft: 0;\n" +
                            "\tright: 0;\n" +
                            "\tbottom: -10px;\n" +
                            "\tcontent: '';\n" +
                            "\twidth: 100%;\n" +
                            "\theight: 2px;\n" +
                            "\tbackground: #30e3ca;\n" +
                            "\tmargin: 0 auto;\n" +
                            "}\n" +
                            "\n" +
                            ".heading-section-white{\n" +
                            "\tcolor: rgba(255,255,255,.8);\n" +
                            "}\n" +
                            ".heading-section-white h2{\n" +
                            "\tfont-family: \n" +
                            "\tline-height: 1;\n" +
                            "\tpadding-bottom: 0;\n" +
                            "}\n" +
                            ".heading-section-white h2{\n" +
                            "\tcolor: #ffffff;\n" +
                            "}\n" +
                            ".heading-section-white .subheading{\n" +
                            "\tmargin-bottom: 0;\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tfont-size: 13px;\n" +
                            "\ttext-transform: uppercase;\n" +
                            "\tletter-spacing: 2px;\n" +
                            "\tcolor: rgba(255,255,255,.4);\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "ul.social{\n" +
                            "\tpadding: 0;\n" +
                            "}\n" +
                            "ul.social li{\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tmargin-right: 10px;\n" +
                            "}\n" +
                            "\n" +
                            "/*FOOTER*/\n" +
                            "\n" +
                            ".footer{\n" +
                            "\tborder-top: 1px solid rgba(0,0,0,.05);\n" +
                            "\tcolor: rgba(0,0,0,.5);\n" +
                            "}\n" +
                            ".footer .heading{\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-size: 20px;\n" +
                            "}\n" +
                            ".footer ul{\n" +
                            "\tmargin: 0;\n" +
                            "\tpadding: 0;\n" +
                            "}\n" +
                            ".footer ul li{\n" +
                            "\tlist-style: none;\n" +
                            "\tmargin-bottom: 10px;\n" +
                            "}\n" +
                            ".footer ul li a{\n" +
                            "\tcolor: rgba(0,0,0,1);\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "@media screen and (max-width: 500px) {\n" +
                            "\n" +
                            "\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "    </style>\n" +
                            "\n" +
                            "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                            "</head>\n" +
                            "\n" +
                            "<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly; background-color: #f1f1f1;\">\n" +
                            "\t<center style=\"width: 100%; background-color: #f1f1f1;\">\n" +
                            "    <div style=\"display: none; font-size: 1px;max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden; mso-hide: all; font-family: sans-serif;\">\n" +
                            "      &zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;\n" +
                            "    </div>\n" +
                            "    <div style=\"max-width: 600px; margin: 0 auto;\" class=\"email-container\">\n" +
                            "    \t<!-- BEGIN BODY -->\n" +
                            "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                            "      \t<tr>\n" +
                            "          <td valign=\"top\" class=\"bg_white\" style=\"padding: 1em 2.5em 0 2.5em;\">\n" +
                            "          \t<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                            "          \t\t<tr>\n" +
                            "          \t\t\t<td class=\"logo\" style=\"text-align: center;\">\n" +
                            "\t\t\t            <h1><a href=\"#\">KIU GYM</a></h1>\n" +
                            "\t\t\t          </td>\n" +
                            "          \t\t</tr>\n" +
                            "          \t</table>\n" +
                            "          </td>\n" +
                            "\t      </tr><!-- end tr -->\n" +
                            "\t\t\t\t<tr>\n" +
                            "          <td valign=\"middle\" class=\"hero bg_white\" style=\"padding: 2em 0 4em 0;\">\n" +
                            "            <table>\n" +
                            "            \t<tr>\n" +
                            "            \t\t<td>\n" +
                            "            \t\t\t<div class=\"text\" style=\"padding: 0 2.5em; text-align: center;\">\n" +
                            "            \t\t\t\t<h2>Hi,Maintenance Has Been</h2>\n" +
                            "            \t\t\t\t<h1 style=\"color: blue\">Created</h1>\n" +
                            "            \t\t\t\t<h3>Maintenance Title</h3>\n" +
                            "\t\t\t\t\t\t\t<h3 style=\"color: blue\">" + title + "</h3>\n" +
                            "            \t\t\t\t<h3>Create By</h3> " + name + " (Instructor)" + "</h3>\n" +
                            "\t\t\t\t\t\t\t<h3>Please Review It !</h3>\n" +
//                            "            \t\t\t\t<p><a href=\"http://13.232.138.190:8087/apps/myTask\" class=\"btn btn-primary\">Go To Portal</a></p>\n" +
                            "            \t\t\t</div>\n" +
                            "            \t\t</td>\n" +
                            "            \t</tr>\n" +
                            "            </table>\n" +
                            "          </td>\n" +
                            "\t      </tr><!-- end tr -->\n" +
                            "      <!-- 1 Column Text + Button : END -->\n" +
                            "      </table>\n" +
                            "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                            "    \n" +
                            "        <tr>\n" +
                            "          <td class=\"bg_light\" style=\"text-align: center;\">\n" +
                            "          \t<p>This ia an auto generated email <a href=\"#\" style=\"color: rgba(0,0,0,.8);\"><i class=\"fa fa-heart\" style=\"font-size:20px;color:red\"></i> By KIU GYM</a></p>\n" +
                            "          </td>\n" +
                            "        </tr>\n" +
                            "      </table>\n" +
                            "\n" +
                            "    </div>\n" +
                            "  </center>\n" +
                            "</body>\n" +
                            "</html>",
                    true);
            emailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendEmailCategoryCreateByInstructor(String toEmail, String title, String name) {
        String subject = "KIU GYM CREATE Category";
        String from = "kiugymportal@gmail.com";


        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(
                    "<!DOCTYPE html>\n" +
                            "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"utf-8\"> <!-- utf-8 works for most cases -->\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width\"> <!-- Forcing initial-scale shouldn't be necessary -->\n" +
                            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> <!-- Use the latest (edge) version of IE rendering engine -->\n" +
                            "    <meta name=\"x-apple-disable-message-reformatting\">  <!-- Disable auto-scale in iOS 10 Mail entirely -->\n" +
                            "    <title></title> <!-- The title tag shows in email notifications, like Android 4.4. -->\n" +
                            "\n" +
                            "    <link href=\"https://fonts.googleapis.com/css?family=Lato:300,400,700\" rel=\"stylesheet\">\n" +
                            "\n" +
                            "    <!-- CSS Reset : BEGIN -->\n" +
                            "    <style>\n" +
                            "\n" +
                            "        /* What it does: Remove spaces around the email design added by some email clients. */\n" +
                            "        /* Beware: It can remove the padding / margin and add a background color to the compose a reply window. */\n" +
                            "        html,\n" +
                            "body {\n" +
                            "    margin: 0 auto !important;\n" +
                            "    padding: 0 !important;\n" +
                            "    height: 100% !important;\n" +
                            "    width: 100% !important;\n" +
                            "    background: #f1f1f1;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Stops email clients resizing small text. */\n" +
                            "* {\n" +
                            "    -ms-text-size-adjust: 100%;\n" +
                            "    -webkit-text-size-adjust: 100%;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Centers email on Android 4.4 */\n" +
                            "div[style*=\"margin: 16px 0\"] {\n" +
                            "    margin: 0 !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Stops Outlook from adding extra spacing to tables. */\n" +
                            "table,\n" +
                            "td {\n" +
                            "    mso-table-lspace: 0pt !important;\n" +
                            "    mso-table-rspace: 0pt !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Fixes webkit padding issue. */\n" +
                            "table {\n" +
                            "    border-spacing: 0 !important;\n" +
                            "    border-collapse: collapse !important;\n" +
                            "    table-layout: fixed !important;\n" +
                            "    margin: 0 auto !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Uses a better rendering method when resizing images in IE. */\n" +
                            "img {\n" +
                            "    -ms-interpolation-mode:bicubic;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Windows 10 Mail from underlining links despite inline CSS. Styles for underlined links should be inline. */\n" +
                            "a {\n" +
                            "    text-decoration: none;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: A work-around for email clients meddling in triggered links. */\n" +
                            "*[x-apple-data-detectors],  /* iOS */\n" +
                            ".unstyle-auto-detected-links *,\n" +
                            ".aBn {\n" +
                            "    border-bottom: 0 !important;\n" +
                            "    cursor: default !important;\n" +
                            "    color: inherit !important;\n" +
                            "    text-decoration: none !important;\n" +
                            "    font-size: inherit !important;\n" +
                            "    font-family: inherit !important;\n" +
                            "    font-weight: inherit !important;\n" +
                            "    line-height: inherit !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Gmail from displaying a download button on large, non-linked images. */\n" +
                            ".a6S {\n" +
                            "    display: none !important;\n" +
                            "    opacity: 0.01 !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Gmail from changing the text color in conversation threads. */\n" +
                            ".im {\n" +
                            "    color: inherit !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* If the above doesn't work, add a .g-img class to any image in question. */\n" +
                            "img.g-img + div {\n" +
                            "    display: none !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Removes right gutter in Gmail iOS app: https://github.com/TedGoas/Cerberus/issues/89  */\n" +
                            "/* Create one of these media queries for each additional viewport size you'd like to fix */\n" +
                            "\n" +
                            "/* iPhone 4, 4S, 5, 5S, 5C, and 5SE */\n" +
                            "@media only screen and (min-device-width: 320px) and (max-device-width: 374px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 320px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "/* iPhone 6, 6S, 7, 8, and X */\n" +
                            "@media only screen and (min-device-width: 375px) and (max-device-width: 413px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 375px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "/* iPhone 6+, 7+, and 8+ */\n" +
                            "@media only screen and (min-device-width: 414px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 414px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "\n" +
                            "    </style>\n" +
                            "\n" +
                            "    <!-- CSS Reset : END -->\n" +
                            "\n" +
                            "    <!-- Progressive Enhancements : BEGIN -->\n" +
                            "    <style>\n" +
                            "\n" +
                            "\t    .primary{\n" +
                            "\tbackground: #30e3ca;\n" +
                            "}\n" +
                            ".bg_white{\n" +
                            "\tbackground: #ffffff;\n" +
                            "}\n" +
                            ".bg_light{\n" +
                            "\tbackground: #fafafa;\n" +
                            "}\n" +
                            ".bg_black{\n" +
                            "\tbackground: #000000;\n" +
                            "}\n" +
                            ".bg_dark{\n" +
                            "\tbackground: rgba(0,0,0,.8);\n" +
                            "}\n" +
                            ".email-section{\n" +
                            "\tpadding:2.5em;\n" +
                            "}\n" +
                            "\n" +
                            "/*BUTTON*/\n" +
                            ".btn{\n" +
                            "\tpadding: 10px 15px;\n" +
                            "\tdisplay: inline-block;\n" +
                            "}\n" +
                            ".btn.btn-primary{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: #30e3ca;\n" +
                            "\tcolor: #ffffff;\n" +
                            "}\n" +
                            ".btn.btn-white{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: #ffffff;\n" +
                            "\tcolor: #000000;\n" +
                            "}\n" +
                            ".btn.btn-white-outline{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: transparent;\n" +
                            "\tborder: 1px solid #fff;\n" +
                            "\tcolor: #fff;\n" +
                            "}\n" +
                            ".btn.btn-black-outline{\n" +
                            "\tborder-radius: 0px;\n" +
                            "\tbackground: transparent;\n" +
                            "\tborder: 2px solid #000;\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-weight: 700;\n" +
                            "}\n" +
                            "\n" +
                            "h1,h2,h3,h4,h5,h6{\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "\tcolor: #000000;\n" +
                            "\tmargin-top: 0;\n" +
                            "\tfont-weight: 400;\n" +
                            "}\n" +
                            "\n" +
                            "body{\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "\tfont-weight: 400;\n" +
                            "\tfont-size: 15px;\n" +
                            "\tline-height: 1.8;\n" +
                            "\tcolor: rgba(0,0,0,.4);\n" +
                            "}\n" +
                            "\n" +
                            "a{\n" +
                            "\tcolor: #30e3ca;\n" +
                            "}\n" +
                            "\n" +
                            "table{\n" +
                            "}\n" +
                            "/*LOGO*/\n" +
                            "\n" +
                            ".logo h1{\n" +
                            "\tmargin: 0;\n" +
                            "}\n" +
                            ".logo h1 a{\n" +
                            "\tcolor: #30e3ca;\n" +
                            "\tfont-size: 24px;\n" +
                            "\tfont-weight: 700;\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "}\n" +
                            "\n" +
                            "/*HERO*/\n" +
                            ".hero{\n" +
                            "\tposition: relative;\n" +
                            "\tz-index: 0;\n" +
                            "}\n" +
                            "\n" +
                            ".hero .text{\n" +
                            "\tcolor: rgba(0,0,0,.3);\n" +
                            "}\n" +
                            ".hero .text h2{\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-size: 40px;\n" +
                            "\tmargin-bottom: 0;\n" +
                            "\tfont-weight: 400;\n" +
                            "\tline-height: 1.4;\n" +
                            "}\n" +
                            ".hero .text h3{\n" +
                            "\tfont-size: 24px;\n" +
                            "\tfont-weight: 300;\n" +
                            "}\n" +
                            ".hero .text h2 span{\n" +
                            "\tfont-weight: 600;\n" +
                            "\tcolor: #30e3ca;\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "/*HEADING SECTION*/\n" +
                            ".heading-section{\n" +
                            "}\n" +
                            ".heading-section h2{\n" +
                            "\tcolor: #000000;\n" +
                            "\tfont-size: 28px;\n" +
                            "\tmargin-top: 0;\n" +
                            "\tline-height: 1.4;\n" +
                            "\tfont-weight: 400;\n" +
                            "}\n" +
                            ".heading-section .subheading{\n" +
                            "\tmargin-bottom: 20px !important;\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tfont-size: 13px;\n" +
                            "\ttext-transform: uppercase;\n" +
                            "\tletter-spacing: 2px;\n" +
                            "\tcolor: rgba(0,0,0,.4);\n" +
                            "\tposition: relative;\n" +
                            "}\n" +
                            ".heading-section .subheading::after{\n" +
                            "\tposition: absolute;\n" +
                            "\tleft: 0;\n" +
                            "\tright: 0;\n" +
                            "\tbottom: -10px;\n" +
                            "\tcontent: '';\n" +
                            "\twidth: 100%;\n" +
                            "\theight: 2px;\n" +
                            "\tbackground: #30e3ca;\n" +
                            "\tmargin: 0 auto;\n" +
                            "}\n" +
                            "\n" +
                            ".heading-section-white{\n" +
                            "\tcolor: rgba(255,255,255,.8);\n" +
                            "}\n" +
                            ".heading-section-white h2{\n" +
                            "\tfont-family: \n" +
                            "\tline-height: 1;\n" +
                            "\tpadding-bottom: 0;\n" +
                            "}\n" +
                            ".heading-section-white h2{\n" +
                            "\tcolor: #ffffff;\n" +
                            "}\n" +
                            ".heading-section-white .subheading{\n" +
                            "\tmargin-bottom: 0;\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tfont-size: 13px;\n" +
                            "\ttext-transform: uppercase;\n" +
                            "\tletter-spacing: 2px;\n" +
                            "\tcolor: rgba(255,255,255,.4);\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "ul.social{\n" +
                            "\tpadding: 0;\n" +
                            "}\n" +
                            "ul.social li{\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tmargin-right: 10px;\n" +
                            "}\n" +
                            "\n" +
                            "/*FOOTER*/\n" +
                            "\n" +
                            ".footer{\n" +
                            "\tborder-top: 1px solid rgba(0,0,0,.05);\n" +
                            "\tcolor: rgba(0,0,0,.5);\n" +
                            "}\n" +
                            ".footer .heading{\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-size: 20px;\n" +
                            "}\n" +
                            ".footer ul{\n" +
                            "\tmargin: 0;\n" +
                            "\tpadding: 0;\n" +
                            "}\n" +
                            ".footer ul li{\n" +
                            "\tlist-style: none;\n" +
                            "\tmargin-bottom: 10px;\n" +
                            "}\n" +
                            ".footer ul li a{\n" +
                            "\tcolor: rgba(0,0,0,1);\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "@media screen and (max-width: 500px) {\n" +
                            "\n" +
                            "\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "    </style>\n" +
                            "\n" +
                            "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                            "</head>\n" +
                            "\n" +
                            "<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly; background-color: #f1f1f1;\">\n" +
                            "\t<center style=\"width: 100%; background-color: #f1f1f1;\">\n" +
                            "    <div style=\"display: none; font-size: 1px;max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden; mso-hide: all; font-family: sans-serif;\">\n" +
                            "      &zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;\n" +
                            "    </div>\n" +
                            "    <div style=\"max-width: 600px; margin: 0 auto;\" class=\"email-container\">\n" +
                            "    \t<!-- BEGIN BODY -->\n" +
                            "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                            "      \t<tr>\n" +
                            "          <td valign=\"top\" class=\"bg_white\" style=\"padding: 1em 2.5em 0 2.5em;\">\n" +
                            "          \t<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                            "          \t\t<tr>\n" +
                            "          \t\t\t<td class=\"logo\" style=\"text-align: center;\">\n" +
                            "\t\t\t            <h1><a href=\"#\">KIU GYM</a></h1>\n" +
                            "\t\t\t          </td>\n" +
                            "          \t\t</tr>\n" +
                            "          \t</table>\n" +
                            "          </td>\n" +
                            "\t      </tr><!-- end tr -->\n" +
                            "\t\t\t\t<tr>\n" +
                            "          <td valign=\"middle\" class=\"hero bg_white\" style=\"padding: 2em 0 4em 0;\">\n" +
                            "            <table>\n" +
                            "            \t<tr>\n" +
                            "            \t\t<td>\n" +
                            "            \t\t\t<div class=\"text\" style=\"padding: 0 2.5em; text-align: center;\">\n" +
                            "            \t\t\t\t<h2>Hi,Category Has Been</h2>\n" +
                            "            \t\t\t\t<h1 style=\"color: blue\">Created</h1>\n" +
                            "            \t\t\t\t<h3>Category Title</h3>\n" +
                            "\t\t\t\t\t\t\t<h3 style=\"color: blue\">" + title + "</h3>\n" +
                            "            \t\t\t\t<h3>Create By</h3> " + name + " (Instructor)" + "</h3>\n" +
                            "\t\t\t\t\t\t\t<h3>Please Review It !</h3>\n" +
//                            "            \t\t\t\t<p><a href=\"http://13.232.138.190:8087/apps/myTask\" class=\"btn btn-primary\">Go To Portal</a></p>\n" +
                            "            \t\t\t</div>\n" +
                            "            \t\t</td>\n" +
                            "            \t</tr>\n" +
                            "            </table>\n" +
                            "          </td>\n" +
                            "\t      </tr><!-- end tr -->\n" +
                            "      <!-- 1 Column Text + Button : END -->\n" +
                            "      </table>\n" +
                            "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                            "    \n" +
                            "        <tr>\n" +
                            "          <td class=\"bg_light\" style=\"text-align: center;\">\n" +
                            "          \t<p>This ia an auto generated email <a href=\"#\" style=\"color: rgba(0,0,0,.8);\"><i class=\"fa fa-heart\" style=\"font-size:20px;color:red\"></i> By KIU GYM</a></p>\n" +
                            "          </td>\n" +
                            "        </tr>\n" +
                            "      </table>\n" +
                            "\n" +
                            "    </div>\n" +
                            "  </center>\n" +
                            "</body>\n" +
                            "</html>",
                    true);
            emailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendEmailMaintenanceEditByInstructor(String toEmail, String title, String name) {
        String subject = "KIU GYM EDITE MAINTENANCE";
        String from = "kiugymportal@gmail.com";


        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(
                    "<!DOCTYPE html>\n" +
                            "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"utf-8\"> <!-- utf-8 works for most cases -->\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width\"> <!-- Forcing initial-scale shouldn't be necessary -->\n" +
                            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> <!-- Use the latest (edge) version of IE rendering engine -->\n" +
                            "    <meta name=\"x-apple-disable-message-reformatting\">  <!-- Disable auto-scale in iOS 10 Mail entirely -->\n" +
                            "    <title></title> <!-- The title tag shows in email notifications, like Android 4.4. -->\n" +
                            "\n" +
                            "    <link href=\"https://fonts.googleapis.com/css?family=Lato:300,400,700\" rel=\"stylesheet\">\n" +
                            "\n" +
                            "    <!-- CSS Reset : BEGIN -->\n" +
                            "    <style>\n" +
                            "\n" +
                            "        /* What it does: Remove spaces around the email design added by some email clients. */\n" +
                            "        /* Beware: It can remove the padding / margin and add a background color to the compose a reply window. */\n" +
                            "        html,\n" +
                            "body {\n" +
                            "    margin: 0 auto !important;\n" +
                            "    padding: 0 !important;\n" +
                            "    height: 100% !important;\n" +
                            "    width: 100% !important;\n" +
                            "    background: #f1f1f1;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Stops email clients resizing small text. */\n" +
                            "* {\n" +
                            "    -ms-text-size-adjust: 100%;\n" +
                            "    -webkit-text-size-adjust: 100%;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Centers email on Android 4.4 */\n" +
                            "div[style*=\"margin: 16px 0\"] {\n" +
                            "    margin: 0 !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Stops Outlook from adding extra spacing to tables. */\n" +
                            "table,\n" +
                            "td {\n" +
                            "    mso-table-lspace: 0pt !important;\n" +
                            "    mso-table-rspace: 0pt !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Fixes webkit padding issue. */\n" +
                            "table {\n" +
                            "    border-spacing: 0 !important;\n" +
                            "    border-collapse: collapse !important;\n" +
                            "    table-layout: fixed !important;\n" +
                            "    margin: 0 auto !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Uses a better rendering method when resizing images in IE. */\n" +
                            "img {\n" +
                            "    -ms-interpolation-mode:bicubic;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Windows 10 Mail from underlining links despite inline CSS. Styles for underlined links should be inline. */\n" +
                            "a {\n" +
                            "    text-decoration: none;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: A work-around for email clients meddling in triggered links. */\n" +
                            "*[x-apple-data-detectors],  /* iOS */\n" +
                            ".unstyle-auto-detected-links *,\n" +
                            ".aBn {\n" +
                            "    border-bottom: 0 !important;\n" +
                            "    cursor: default !important;\n" +
                            "    color: inherit !important;\n" +
                            "    text-decoration: none !important;\n" +
                            "    font-size: inherit !important;\n" +
                            "    font-family: inherit !important;\n" +
                            "    font-weight: inherit !important;\n" +
                            "    line-height: inherit !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Gmail from displaying a download button on large, non-linked images. */\n" +
                            ".a6S {\n" +
                            "    display: none !important;\n" +
                            "    opacity: 0.01 !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Gmail from changing the text color in conversation threads. */\n" +
                            ".im {\n" +
                            "    color: inherit !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* If the above doesn't work, add a .g-img class to any image in question. */\n" +
                            "img.g-img + div {\n" +
                            "    display: none !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Removes right gutter in Gmail iOS app: https://github.com/TedGoas/Cerberus/issues/89  */\n" +
                            "/* Create one of these media queries for each additional viewport size you'd like to fix */\n" +
                            "\n" +
                            "/* iPhone 4, 4S, 5, 5S, 5C, and 5SE */\n" +
                            "@media only screen and (min-device-width: 320px) and (max-device-width: 374px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 320px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "/* iPhone 6, 6S, 7, 8, and X */\n" +
                            "@media only screen and (min-device-width: 375px) and (max-device-width: 413px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 375px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "/* iPhone 6+, 7+, and 8+ */\n" +
                            "@media only screen and (min-device-width: 414px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 414px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "\n" +
                            "    </style>\n" +
                            "\n" +
                            "    <!-- CSS Reset : END -->\n" +
                            "\n" +
                            "    <!-- Progressive Enhancements : BEGIN -->\n" +
                            "    <style>\n" +
                            "\n" +
                            "\t    .primary{\n" +
                            "\tbackground: #30e3ca;\n" +
                            "}\n" +
                            ".bg_white{\n" +
                            "\tbackground: #ffffff;\n" +
                            "}\n" +
                            ".bg_light{\n" +
                            "\tbackground: #fafafa;\n" +
                            "}\n" +
                            ".bg_black{\n" +
                            "\tbackground: #000000;\n" +
                            "}\n" +
                            ".bg_dark{\n" +
                            "\tbackground: rgba(0,0,0,.8);\n" +
                            "}\n" +
                            ".email-section{\n" +
                            "\tpadding:2.5em;\n" +
                            "}\n" +
                            "\n" +
                            "/*BUTTON*/\n" +
                            ".btn{\n" +
                            "\tpadding: 10px 15px;\n" +
                            "\tdisplay: inline-block;\n" +
                            "}\n" +
                            ".btn.btn-primary{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: #30e3ca;\n" +
                            "\tcolor: #ffffff;\n" +
                            "}\n" +
                            ".btn.btn-white{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: #ffffff;\n" +
                            "\tcolor: #000000;\n" +
                            "}\n" +
                            ".btn.btn-white-outline{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: transparent;\n" +
                            "\tborder: 1px solid #fff;\n" +
                            "\tcolor: #fff;\n" +
                            "}\n" +
                            ".btn.btn-black-outline{\n" +
                            "\tborder-radius: 0px;\n" +
                            "\tbackground: transparent;\n" +
                            "\tborder: 2px solid #000;\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-weight: 700;\n" +
                            "}\n" +
                            "\n" +
                            "h1,h2,h3,h4,h5,h6{\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "\tcolor: #000000;\n" +
                            "\tmargin-top: 0;\n" +
                            "\tfont-weight: 400;\n" +
                            "}\n" +
                            "\n" +
                            "body{\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "\tfont-weight: 400;\n" +
                            "\tfont-size: 15px;\n" +
                            "\tline-height: 1.8;\n" +
                            "\tcolor: rgba(0,0,0,.4);\n" +
                            "}\n" +
                            "\n" +
                            "a{\n" +
                            "\tcolor: #30e3ca;\n" +
                            "}\n" +
                            "\n" +
                            "table{\n" +
                            "}\n" +
                            "/*LOGO*/\n" +
                            "\n" +
                            ".logo h1{\n" +
                            "\tmargin: 0;\n" +
                            "}\n" +
                            ".logo h1 a{\n" +
                            "\tcolor: #30e3ca;\n" +
                            "\tfont-size: 24px;\n" +
                            "\tfont-weight: 700;\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "}\n" +
                            "\n" +
                            "/*HERO*/\n" +
                            ".hero{\n" +
                            "\tposition: relative;\n" +
                            "\tz-index: 0;\n" +
                            "}\n" +
                            "\n" +
                            ".hero .text{\n" +
                            "\tcolor: rgba(0,0,0,.3);\n" +
                            "}\n" +
                            ".hero .text h2{\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-size: 40px;\n" +
                            "\tmargin-bottom: 0;\n" +
                            "\tfont-weight: 400;\n" +
                            "\tline-height: 1.4;\n" +
                            "}\n" +
                            ".hero .text h3{\n" +
                            "\tfont-size: 24px;\n" +
                            "\tfont-weight: 300;\n" +
                            "}\n" +
                            ".hero .text h2 span{\n" +
                            "\tfont-weight: 600;\n" +
                            "\tcolor: #30e3ca;\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "/*HEADING SECTION*/\n" +
                            ".heading-section{\n" +
                            "}\n" +
                            ".heading-section h2{\n" +
                            "\tcolor: #000000;\n" +
                            "\tfont-size: 28px;\n" +
                            "\tmargin-top: 0;\n" +
                            "\tline-height: 1.4;\n" +
                            "\tfont-weight: 400;\n" +
                            "}\n" +
                            ".heading-section .subheading{\n" +
                            "\tmargin-bottom: 20px !important;\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tfont-size: 13px;\n" +
                            "\ttext-transform: uppercase;\n" +
                            "\tletter-spacing: 2px;\n" +
                            "\tcolor: rgba(0,0,0,.4);\n" +
                            "\tposition: relative;\n" +
                            "}\n" +
                            ".heading-section .subheading::after{\n" +
                            "\tposition: absolute;\n" +
                            "\tleft: 0;\n" +
                            "\tright: 0;\n" +
                            "\tbottom: -10px;\n" +
                            "\tcontent: '';\n" +
                            "\twidth: 100%;\n" +
                            "\theight: 2px;\n" +
                            "\tbackground: #30e3ca;\n" +
                            "\tmargin: 0 auto;\n" +
                            "}\n" +
                            "\n" +
                            ".heading-section-white{\n" +
                            "\tcolor: rgba(255,255,255,.8);\n" +
                            "}\n" +
                            ".heading-section-white h2{\n" +
                            "\tfont-family: \n" +
                            "\tline-height: 1;\n" +
                            "\tpadding-bottom: 0;\n" +
                            "}\n" +
                            ".heading-section-white h2{\n" +
                            "\tcolor: #ffffff;\n" +
                            "}\n" +
                            ".heading-section-white .subheading{\n" +
                            "\tmargin-bottom: 0;\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tfont-size: 13px;\n" +
                            "\ttext-transform: uppercase;\n" +
                            "\tletter-spacing: 2px;\n" +
                            "\tcolor: rgba(255,255,255,.4);\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "ul.social{\n" +
                            "\tpadding: 0;\n" +
                            "}\n" +
                            "ul.social li{\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tmargin-right: 10px;\n" +
                            "}\n" +
                            "\n" +
                            "/*FOOTER*/\n" +
                            "\n" +
                            ".footer{\n" +
                            "\tborder-top: 1px solid rgba(0,0,0,.05);\n" +
                            "\tcolor: rgba(0,0,0,.5);\n" +
                            "}\n" +
                            ".footer .heading{\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-size: 20px;\n" +
                            "}\n" +
                            ".footer ul{\n" +
                            "\tmargin: 0;\n" +
                            "\tpadding: 0;\n" +
                            "}\n" +
                            ".footer ul li{\n" +
                            "\tlist-style: none;\n" +
                            "\tmargin-bottom: 10px;\n" +
                            "}\n" +
                            ".footer ul li a{\n" +
                            "\tcolor: rgba(0,0,0,1);\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "@media screen and (max-width: 500px) {\n" +
                            "\n" +
                            "\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "    </style>\n" +
                            "\n" +
                            "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                            "</head>\n" +
                            "\n" +
                            "<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly; background-color: #f1f1f1;\">\n" +
                            "\t<center style=\"width: 100%; background-color: #f1f1f1;\">\n" +
                            "    <div style=\"display: none; font-size: 1px;max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden; mso-hide: all; font-family: sans-serif;\">\n" +
                            "      &zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;\n" +
                            "    </div>\n" +
                            "    <div style=\"max-width: 600px; margin: 0 auto;\" class=\"email-container\">\n" +
                            "    \t<!-- BEGIN BODY -->\n" +
                            "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                            "      \t<tr>\n" +
                            "          <td valign=\"top\" class=\"bg_white\" style=\"padding: 1em 2.5em 0 2.5em;\">\n" +
                            "          \t<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                            "          \t\t<tr>\n" +
                            "          \t\t\t<td class=\"logo\" style=\"text-align: center;\">\n" +
                            "\t\t\t            <h1><a href=\"#\">KIU GYM</a></h1>\n" +
                            "\t\t\t          </td>\n" +
                            "          \t\t</tr>\n" +
                            "          \t</table>\n" +
                            "          </td>\n" +
                            "\t      </tr><!-- end tr -->\n" +
                            "\t\t\t\t<tr>\n" +
                            "          <td valign=\"middle\" class=\"hero bg_white\" style=\"padding: 2em 0 4em 0;\">\n" +
                            "            <table>\n" +
                            "            \t<tr>\n" +
                            "            \t\t<td>\n" +
                            "            \t\t\t<div class=\"text\" style=\"padding: 0 2.5em; text-align: center;\">\n" +
                            "            \t\t\t\t<h2>Hi,Maintenance Has Been</h2>\n" +
                            "            \t\t\t\t<h1 style=\"color: blue\">Edited</h1>\n" +
                            "            \t\t\t\t<h3>Maintenance Title</h3>\n" +
                            "\t\t\t\t\t\t\t<h3 style=\"color: blue\">" + title + "</h3>\n" +
                            "            \t\t\t\t<h3>Edited By</h3> " + name + " (Instructor)" + "</h3>\n" +
                            "\t\t\t\t\t\t\t<h3>Please Review It !</h3>\n" +
//                            "            \t\t\t\t<p><a href=\"http://13.232.138.190:8087/apps/myTask\" class=\"btn btn-primary\">Go To Portal</a></p>\n" +
                            "            \t\t\t</div>\n" +
                            "            \t\t</td>\n" +
                            "            \t</tr>\n" +
                            "            </table>\n" +
                            "          </td>\n" +
                            "\t      </tr><!-- end tr -->\n" +
                            "      <!-- 1 Column Text + Button : END -->\n" +
                            "      </table>\n" +
                            "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                            "    \n" +
                            "        <tr>\n" +
                            "          <td class=\"bg_light\" style=\"text-align: center;\">\n" +
                            "          \t<p>This ia an auto generated email <a href=\"#\" style=\"color: rgba(0,0,0,.8);\"><i class=\"fa fa-heart\" style=\"font-size:20px;color:red\"></i> By KIU GYM</a></p>\n" +
                            "          </td>\n" +
                            "        </tr>\n" +
                            "      </table>\n" +
                            "\n" +
                            "    </div>\n" +
                            "  </center>\n" +
                            "</body>\n" +
                            "</html>",
                    true);
            emailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
