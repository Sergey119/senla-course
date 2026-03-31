public class MenuItem {
    private String title;
    private IAction action;
    private Menu nextMenu;

    public MenuItem(String title, IAction action, Menu nextMenu) {
        this.title = title;
        this.action = action;
        this.nextMenu = nextMenu;
    }

    public void doAction() {
        if (action != null) {
            action.execute();
        }
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public IAction getAction() { return action; }
    public void setAction(IAction action) { this.action = action; }
    public boolean hasAction() {
        return action != null;
    }

    public Menu getNextMenu() { return nextMenu; }
    public void setNextMenu(Menu nextMenu) { this.nextMenu = nextMenu; }
    public boolean hasNextMenu() {
        return nextMenu != null;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "title='" + title + '\'' +
                ", action=" + action +
                ", nextMenu=" + nextMenu +
                '}';
    }
}