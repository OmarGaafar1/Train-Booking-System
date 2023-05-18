public class Person {
    public int ID;
    public String Name;
    public String Email;
    public String Password;

    public Person(){}
    public Person(Person other) {
        this.Name = other.Name;
        this.Email = other.Email;
        this.Password = other.Password;
        this.ID = other.ID;
    }
}
