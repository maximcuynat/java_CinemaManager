public class Person
{
    private String firstName;
    private String lastName;

    Person() {
        this.firstName = "";
        this.lastName = "";
    }

    Person(String firstName, String lastName)
    {
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    private void setFirstName(String firstName){
        if(!firstName.isEmpty())
        {
            this.firstName = firstName;
        } else {
            this.firstName = "No firstname";
        }
    }

    private void setLastName(String lastName){
        if(!lastName.isEmpty())
        {
            this.lastName = lastName;
        } else {
            this.lastName = "No lastName";
        }
    }
}