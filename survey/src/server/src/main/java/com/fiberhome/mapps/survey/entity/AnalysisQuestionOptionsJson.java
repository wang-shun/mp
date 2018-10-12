package com.fiberhome.mapps.survey.entity;

public class AnalysisQuestionOptionsJson
{

    private String id;

    private String option;

    private String options;

    private Long   sequ;

    private String selectedPercent;

    private Long   selected;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return option
     */
    public String getOption()
    {
        return option;
    }

    /**
     * @param option
     */
    public void setOption(String option)
    {
        this.option = option;
    }

    public String getOptions()
    {
        return options;
    }

    public void setOptions(String options)
    {
        this.options = options;
        this.option = options;
    }

    /**
     * @return sequ
     */
    public Long getSequ()
    {
        return sequ;
    }

    /**
     * @param sequ
     */
    public void setSequ(Long sequ)
    {
        this.sequ = sequ;
    }

    public String getSelectedPercent()
    {
        return selectedPercent;
    }

    public void setSelectedPercent(String selectedPercent)
    {
        this.selectedPercent = selectedPercent;
    }

    public Long getSelected()
    {
        return selected;
    }

    public void setSelected(Long selected)
    {
        this.selected = selected;
    }

}