package canary.paper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService {

    private UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User findUserById(long id) {
        return userRepo.getOne(id);
    }

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    public void addUser(User user) {
        userRepo.save(user);
    }

    public void updateUser(User user) {
        User userToUpdate = userRepo.getOne(user.getId());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(user.getPassword());

        userRepo.save(userToUpdate);
    }

    public void removeUser(User user) {
        userRepo.delete(user);
    }
}