package simulator.GUI.swing.elements;

public abstract interface UI
{
  public abstract void processMenu(UIMenu paramUIMenu);

  public abstract String[] processForm(UIForm paramUIForm);

  public abstract void displayMessage(String paramString);

  public abstract void displayError(String paramString);
}