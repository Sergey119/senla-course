public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        ILineStep carBodyLineStep = new CarBodyLineStep();
        ILineStep chassisLineStep = new ChassisLineStep();
        ILineStep engineLineStep = new EngineLineStep();

        AssemblyLine assemblyLine = new AssemblyLine(carBodyLineStep, chassisLineStep, engineLineStep);

        CarProduct carProduct = (CarProduct) assemblyLine.assemblyProduct(new CarProduct());
        System.out.println(carProduct);
    }
}