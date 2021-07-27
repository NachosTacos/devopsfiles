package canary.paper;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RedirectController {

    @Autowired
    private UserService userService;

    @GetMapping(value = { "/", "/home" })
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");
        return mav;
    }

    @GetMapping(value = "/users")
    public ModelAndView listUsers() {
        List<User> users = userService.findAllUsers();
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", users);
        mav.setViewName("users");
        return mav;
    }

    @GetMapping(value = "/create")
    public ModelAndView createUser() {
        ModelAndView mav = new ModelAndView();
        User user = new User();
        mav.addObject("user", user);
        mav.setViewName("create");
        return mav;
    }

    @PostMapping(value = "/create")
    public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();
        User existingUser = userService.findUserByEmail(user.getEmail());
        if (existingUser != null) {
            bindingResult.rejectValue("email", "error.user", "This email is already in use.");
        }
        if (bindingResult.hasErrors()) {
            mav.setViewName("create");
        } else {
            userService.addUser(user);
            return listUsers();
        }
        return mav;
    }

    @GetMapping(value = "/delete")
    public ModelAndView confirmDelete(long id) {
        ModelAndView mav = new ModelAndView();
        User user = userService.findUserById(id);
        mav.addObject("user", user);
        mav.setViewName("delete");
        return mav;
    }

    @PostMapping(value = "/delete")
    public String deleteUser(long id) {
        User user = userService.findUserById(id);
        userService.removeUser(user);
        return "redirect:/users";
    }

    @GetMapping(value = "/edit")
    public ModelAndView editUser(long id) {
        ModelAndView mav = new ModelAndView();
        User user = userService.findUserById(id);
        mav.addObject("user", user);
        mav.setViewName("edit");
        return mav;
    }

    @PostMapping(value = "/edit")
    public ModelAndView updateUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();
        if (bindingResult.hasErrors()) {
            mav.setViewName("/edit");
        } else {
            userService.updateUser(user);
            return listUsers();
        }
        return mav;
    }

    @GetMapping(value = "/details")
    public ModelAndView showDetails(long id) {
        ModelAndView mav = new ModelAndView();
        User user = userService.findUserById(id);
        mav.addObject("user", user);
        mav.setViewName("details");
        return mav;
    }

}