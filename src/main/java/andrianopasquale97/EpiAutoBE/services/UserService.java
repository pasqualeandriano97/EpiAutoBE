package andrianopasquale97.EpiAutoBE.services;

import andrianopasquale97.EpiAutoBE.entities.User;
import andrianopasquale97.EpiAutoBE.exceptions.NotFoundException;
import andrianopasquale97.EpiAutoBE.payloads.UserDTO;
import andrianopasquale97.EpiAutoBE.payloads.UserRespDTO;
import andrianopasquale97.EpiAutoBE.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserRespDTO save (UserDTO user) {
       this.userDAO.save(new User(user.name(), user.surname(), user.email(),passwordEncoder.encode(user.password())));
        return new UserRespDTO(user.name(),user.surname(),user.email());
    }

    public Page<User> getAll(int page, int size,String sortBy) {
        if(size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userDAO.findAll(pageable);
    }

    public User getById(int id) {
        return this.userDAO.findById(id).orElseThrow(() -> new NotFoundException("Utente non trovato"));
    }

    public UserDTO getByNameAndSurname(String name, String surname) {
        User findUser = this.userDAO.findBySurnameAndName(name, surname).orElseThrow(() -> new NotFoundException("Utente non trovato"));
        return new UserDTO(findUser.getName(), findUser.getSurname(),findUser.getEmail(), findUser.getEmail());
    }
    public User findByEmail(String email) {
        return this.userDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente non trovato"));
    }

    public String findByIdAndDelete(int id) {
        User user = this.userDAO.findById(id).orElseThrow(() -> new NotFoundException("Utente non trovato"));
        this.userDAO.delete(user);
        return "Utente eliminato con successo";
    }

    public UserDTO findByIdAndUpdate(int id, UserDTO user) {
        User currentUser = this.userDAO.findById(id).orElseThrow(() -> new NotFoundException("Utente non trovato"));
        currentUser.setName(user.name());
        currentUser.setSurname(user.surname());
        currentUser.setEmail(user.email());
        currentUser.setPassword(passwordEncoder.encode(user.password()));
        this.userDAO.save(currentUser);
        return user;
    }
}
