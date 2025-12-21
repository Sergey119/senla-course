public class CarProduct implements IProduct{
    private CarBodyPart carBodyPart;
    private ChassisPart chassisPart;
    private EnginePart enginePart;

    public CarProduct(){
        System.out.println("The car assembly process has started.");
    }

    @Override
    public void installFirstPart(IProductPart part) {
        System.out.println("Installing first part: " + part);
        this.carBodyPart = (CarBodyPart) part;
        System.out.println("The " + this.carBodyPart + " part has been installed.");
    }
    @Override
    public void installSecondPart(IProductPart part) {
        System.out.println("Installing second part: " + part);
        this.chassisPart = (ChassisPart) part;
        System.out.println("The " + this.chassisPart + " part has been installed.");
    }
    @Override
    public void installThirdPart(IProductPart part) {
        System.out.println("Installing third part: " + part);
        this.enginePart = (EnginePart) part;
        System.out.println("The " + this.enginePart + " part has been installed.");
    }

    @Override
    public String toString() {
        return "CarProduct{" +
                "carBodyPart=" + carBodyPart +
                ", chassisPart=" + chassisPart +
                ", enginePart=" + enginePart +
                '}';
    }
}
