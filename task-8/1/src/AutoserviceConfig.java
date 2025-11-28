public class AutoserviceConfig {

    @ConfigProperty(propertyName = "add.remove.free.car.places.permission", type = Boolean.class)
    private boolean addRemoveFreeCarPlacesPermission;

    @ConfigProperty(propertyName = "shift.order.time.permission", type = Boolean.class)
    private boolean shiftOrderTimePermission;

    @ConfigProperty(propertyName = "remove.order.permission", type = Boolean.class)
    private boolean removeOrderPermission;

    public boolean isAddRemoveFreeCarPlacesPermission() {  return addRemoveFreeCarPlacesPermission; }
    public boolean isShiftOrderTimePermission() {  return shiftOrderTimePermission;  }
    public boolean isRemoveOrderPermission() { return removeOrderPermission; }
}