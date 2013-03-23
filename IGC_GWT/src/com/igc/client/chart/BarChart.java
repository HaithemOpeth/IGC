package com.igc.client.chart;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
 
public class BarChart implements IsWidget {
	
	private AllData allData=new AllData();
 
  public interface DataPropertyAccess extends PropertyAccess<Data> {
    ValueProvider<Data, Integer> data1();
 
    ValueProvider<Data, String> name();
 
    @Path("name")
    ModelKeyProvider<Data> nameKey();
  }
 
  private static final DataPropertyAccess dataAccess = GWT.create(DataPropertyAccess.class);
 
  @Override
  public Widget asWidget() {
	  List<Data> allprog=allData.getProgLang();
    final ListStore<Data> store = new ListStore<Data>(dataAccess.nameKey());
    store.addAll(allprog);
    
 
    final Chart<Data> chart = new Chart<Data>();
    chart.setStore(store);
    chart.setShadowChart(true);
     
    Gradient background = new Gradient("backgroundGradient", 0);
    background.addStop(0, new RGB("#ffffff"));
    background.addStop(100, new RGB("#eaf1f8"));
    chart.addGradient(background);
    chart.setBackground(background);
 
    NumericAxis<Data> axis = new NumericAxis<Data>();
    axis.setPosition(Position.BOTTOM);
    axis.addField(dataAccess.data1());
    TextSprite title = new TextSprite("Number of Hits");
    title.setFontSize(18);
    axis.setTitleConfig(title);
    axis.setDisplayGrid(true);
    axis.setMinimum(0);
    axis.setMaximum((maxData(allprog))+10);
    chart.addAxis(axis);
 
    CategoryAxis<Data, String> catAxis = new CategoryAxis<Data, String>();
    catAxis.setPosition(Position.LEFT);
    catAxis.setField(dataAccess.name());
    title = new TextSprite("Programming Language");
    title.setFontSize(18);
    catAxis.setTitleConfig(title);
    chart.addAxis(catAxis);
 
    final BarSeries<Data> bar = new BarSeries<Data>();
    bar.setYAxisPosition(Position.BOTTOM);
    bar.addYField(dataAccess.data1());
    bar.addColor(new RGB(148,174,10));
    bar.setHighlighting(true);
    chart.addSeries(bar);
 
   
    ToggleButton animation = new ToggleButton("Animate");
    animation.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      @Override
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        chart.setAnimated(event.getValue());
      }
    });
    animation.setValue(true, true);
    ToggleButton shadow = new ToggleButton("Shadow");
    shadow.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      @Override
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        chart.setShadowChart(event.getValue());
        chart.redrawChart();
      }
    });
    shadow.setValue(true);
 
    ToolBar toolBar = new ToolBar();
  
    toolBar.add(animation);
    toolBar.add(shadow);
 
    ContentPanel panel = new FramedPanel();
    panel.getElement().getStyle().setMargin(10, Unit.PX);
    panel.setCollapsible(true);
    panel.setHeadingText("Bar Chart");
    panel.setPixelSize(500, 400);
    panel.setBodyBorder(true);
     
    final Resizable resize = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
    resize.setMinHeight(400);
    resize.setMinWidth(400);
     
    panel.addExpandHandler(new ExpandHandler() {
      @Override
      public void onExpand(ExpandEvent event) {
        resize.setEnabled(true);
      }
    });
    panel.addCollapseHandler(new CollapseHandler() {
      @Override
      public void onCollapse(CollapseEvent event) {
        resize.setEnabled(false);
      }
    });
    new Draggable(panel, panel.getHeader()).setUseProxy(false);
 
    VerticalLayoutContainer layout = new VerticalLayoutContainer();
    panel.add(layout);
 
    toolBar.setLayoutData(new VerticalLayoutData(1, -1));
    layout.add(toolBar);
 
    chart.setLayoutData(new VerticalLayoutData(1, 1));
    layout.add(chart);
 
    return panel;
  }
  
  public int maxData(List<Data> all)
  {
	  int max =all.get(0).getData1();
			for (Data data : all) {
				if(data.getData1() > max)
					max=data.getData1();
			}
			return max;
  }


}