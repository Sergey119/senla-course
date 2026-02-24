import java.util.List;

public class ProhibitionToShiftOrderTimeHandler implements IPropertyAccessResponseHandler<List<Integer>>{
    @Override
    public void handleResponse(List<Integer> arg) {
        throw new UnsupportedOperationException("Access to shift order time is prohibited.");
    }
}
