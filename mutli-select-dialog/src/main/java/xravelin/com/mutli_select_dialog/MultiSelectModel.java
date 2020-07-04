package xravelin.com.mutli_select_dialog;

public class MultiSelectModel {
    private String id;
    private String name;
    private Boolean isSelected;

    public MultiSelectModel(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Boolean getSelected() {
        return isSelected;
    }

    void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
