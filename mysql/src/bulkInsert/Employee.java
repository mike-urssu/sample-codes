package bulkInsert;

import java.time.LocalDate;

public class Employee {
    private final int empNo;
    private final LocalDate birthDate;
    private final String firstName;
    private final String lastName;
    private final char gender;
    private final LocalDate hireDate;

    public Employee(int empNo, LocalDate birthDate, String firstName, String lastName, char gender, LocalDate hireDate) {
        this.empNo = empNo;
        this.birthDate = birthDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.hireDate = hireDate;
    }

    public int getEmpNo() {
        return empNo;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public char getGender() {
        return gender;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    @Override
    public String toString() {
        return empNo
                + "," + birthDate
                + "," + firstName
                + "," + lastName
                + "," + gender
                + "," + hireDate;
    }
}
