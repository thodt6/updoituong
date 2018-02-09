/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unibro.view;

import java.io.Serializable;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GuestPreferences implements Serializable {

    private String layout = "";

    private String theme = "";

    private String menuMode = "overlay";

    @PostConstruct
    public void init() {

    }

    public String getTheme() {
//        Calendar cal=Calendar.getInstance();
//        int hour=cal.get(Calendar.HOUR_OF_DAY);
//        if(hour>6 && hour <= 11){
//            theme="blue";
//        }
//        if(hour>11 && hour <=16){
//            theme="orange";
//        }
//        if(hour>16 && hour <=20){
//            theme="green";
//        }
//        if(hour>20 && hour <=24){
//            theme="light-blue";
//        }
//        if(hour>=0 && hour <=6){
//            theme="purple";
//        }
        if(!theme.equals("")){
            return theme;
        }
        String[] theme_arr = {"green", "teal", "blue", "amber", "purple", "turquoise", "bluegrey"};
        Random rand = new Random();

        theme = theme_arr[rand.nextInt(theme_arr.length)];

        return theme;
    }

    /**
     * @return the layout
     */
    public String getLayout() {
        if(!layout.equals("")){
            return layout;
        }
        String[] layout_arr = {"dark", "turquoise", "green", "blue", "rose", "teal", "blue", "bluegrey", "cosmic", "beach", "flow", "fly", "nepal"};
        Random rand = new Random();

        layout = layout_arr[rand.nextInt(layout_arr.length)];
        return layout;
    }

    /**
     * @param layout the layout to set
     */
    public void setLayout(String layout) {
        this.layout = layout;
    }

    /**
     * @return the menuMode
     */
    public String getMenuMode() {
        return menuMode;
    }

    /**
     * @param menuMode the menuMode to set
     */
    public void setMenuMode(String menuMode) {
        this.menuMode = menuMode;
    }

}
