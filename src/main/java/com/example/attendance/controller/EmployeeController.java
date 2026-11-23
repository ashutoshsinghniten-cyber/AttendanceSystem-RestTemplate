package com.example.attendance.controller;
import com.example.attendance.dto.EmployeeRequest;
import com.example.attendance.dto.EmployeeForm;
import com.example.attendance.entity.Employee;
import com.example.attendance.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "employees/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("employee", new EmployeeForm());
        return "employees/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Employee existing = employeeService.findById(id);
        EmployeeForm form = new EmployeeForm();
        form.setId(existing.getId());
        form.setName(existing.getName());
        form.setEmail(existing.getEmail());
        form.setDepartment(existing.getDepartment());
        model.addAttribute("employee", form);
        return "employees/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("employee") EmployeeForm form,
                       BindingResult result, RedirectAttributes redirect) {
        Employee employee = new Employee(form.getName(), form.getEmail(), form.getDepartment());
        employee.setId(form.getId());
        if (!result.hasFieldErrors("email") && employeeService.emailTaken(employee)) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }
        if (result.hasErrors()) {
            return "employees/form";
        }
        employeeService.save(employee);
        redirect.addFlashAttribute("message", "Employee saved successfully");
        return "redirect:/employees";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        employeeService.delete(id);
        redirect.addFlashAttribute("message", "Employee deleted");
        return "redirect:/employees";
    }
}
