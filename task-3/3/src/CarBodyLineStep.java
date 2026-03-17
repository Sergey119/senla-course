public class CarBodyLineStep implements ILineStep{
    @Override
    public IProductPart buildProductPart() {
        return new CarBodyPart();
    }
}
