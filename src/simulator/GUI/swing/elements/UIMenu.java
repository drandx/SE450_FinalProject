package simulator.GUI.swing.elements;

public final class UIMenu
{
  private final String _heading;
  private final Pair[] _menu;

  UIMenu(String heading, Pair[] menu)
  {
    this._heading = heading;
    this._menu = menu;
  }
  public int size() {
    return this._menu.length;
  }
  public String getHeading() {
    return this._heading;
  }
  public String getPrompt(int i) {
    return this._menu[i].prompt;
  }
  public void runAction(int i) {
    this._menu[i].action.run();
  }

  static final class Pair
  {
    final String prompt;
    final UIMenuAction action;

    Pair(String thePrompt, UIMenuAction theAction)
    {
      this.prompt = thePrompt;
      this.action = theAction;
    }
  }
}