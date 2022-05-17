package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.util.List;

public interface UserDAOInterface {

    /** ---------------------------------------------------------------------------------------------------------------
     * This method creates a new User entry in DB
     *
     * @param user .username the user username, must be not null and of length <= 30,
     *             .password the user password, must be not null and of length <= 30
     *             .role the user role, must be not null and of length <= 13
     *
     * @return User object with new id and specified username, password, role,
     *         null if insert goes wrong
     *
     * @throws MissingDAOParameterException if user is null
     * @throws MissingDAOParameterException if user.username is null
     * @throws MissingDAOParameterException if user.password is null
     * @throws MissingDAOParameterException if user.role is null
     * @throws InvalidDAOParameterException if user.username length is > 30
     * @throws InvalidDAOParameterException if user.password length is > 30
     * @throws InvalidDAOParameterException if user.role length is > 13
     * ---------------------------------------------------------------------------------------------------------------
     */
    User addUser(User user) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** ------------------------------------------------------------------
     * This method removes a User entry from DB
     *
     * @param userId the id of User to remove, must be not null and > 0
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * @throws MissingDAOParameterException if userId is null
     * @throws InvalidDAOParameterException if userId is <= 0
     * -------------------------------------------------------------------
     */
    boolean removeUser(Integer userId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------------
     * This method returns the list of Users in DB
     *
     * @return list of all Users in DB. List can be empty
     * --------------------------------------------------------------
     */
    List<User> getUsers();

    /** -----------------------------------------------------------------------
     * This method returns the User in DB with given id
     *
     * @param userId the id of the user, must be not null and > 0
     *
     * @return User with corresponding id in DB,
     *         null if user isn't found
     *
     * @throws MissingDAOParameterException if userId is null
     * @throws InvalidDAOParameterException if userId is <= 0
     * -----------------------------------------------------------------------
     */
    User getUserById(Integer userId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -----------------------------------------------------------------------
     * This method returns the User in DB with given id
     *
     * @param username the username of the user, must be not null and of length <= 30
     *
     * @return User with corresponding username in DB,
     *         null if user isn't found
     *
     * @throws MissingDAOParameterException if username is null
     * @throws InvalidDAOParameterException if username length is > 30
     * -----------------------------------------------------------------------
     */
    User getUserByUsername(String username) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------------------------
     * This method changes a User entry for given parameters only
     *
     * @param user .id the id of the user, must be not null and > 0,
     *             .role the new user role, must be not null and of length <= 13
     *
     * @return true if update succeeds,
     *         false if update fails
     *
     * @throws MissingDAOParameterException if user is null
     * @throws MissingDAOParameterException if user.id is null
     * @throws MissingDAOParameterException if user.role is null
     * @throws InvalidDAOParameterException if user.id is <= 0
     * @throws InvalidDAOParameterException if user.role length is > 13
     * --------------------------------------------------------------------------
     */
    boolean updateUser(User user) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------
     * This method removes all User entries from DB
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * ---------------------------------------------------------
     */
    boolean resetUsers();
}
