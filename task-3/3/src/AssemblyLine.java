public class AssemblyLine {
    private ILineStep step1;
    private ILineStep step2;
    private ILineStep step3;

    public AssemblyLine(ILineStep step1, ILineStep step2, ILineStep step3) {
        this.step1 = step1;
        this.step2 = step2;
        this.step3 = step3;
        System.out.println("Assembly line created.");
    }

    public IProduct assemblyProduct(IProduct product) {
        System.out.println("Assembling product.");
        product.installFirstPart(step1.buildProductPart());
        product.installSecondPart(step2.buildProductPart());
        product.installThirdPart(step3.buildProductPart());
        System.out.println("Product assembly completed.");
        return product;
    }
}
