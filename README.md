# AuraSync Android App 

## Smart Home Automation using Ambient Light Sensor
 
#### Sensors involved in the project: 

    Ambient Light Sensor

#### Main goal:

    We propose a system for home automation using an ambient 
	light sensor which can be used to control the intensity 
	of operation of lighting and overall ambience for any given
	place. The intent of this application would be to optimize 
	the power consumption and cost reductions to power and utility
	bills.

#### Application scenario design :

    Every mobile device today is equipped with an ambient light 
	sensor with a sole reason to optimize battery consumption 
	and save the overall limited battery power of the device. 
	This sensor can be leveraged to control the power consumption
	of a house, office or any commercial building that features 
	smart home capabilities. Having the smart devices operate on
	a reduced power consumption mode when not in use can lead to 
	tremendous savings in terms of resource consumption.
    
	To achieve this, we would develop an android application using
	ambient light sensor which measures the intensity of surrounding
	light and automatically adjusts the lighting to create a perfect
	and pleasing ambience. The implementation of the functionality 
	would increase the lighting if the place of interest has low 
	lighting as opposed to the screen brightness control feature in 
	mobile devices which increases the brightness when placed in a 
	bright lighting. An API would be setup to which the readings 
	from ambient sensor would be reported at a specific frequency 
	(for e.g. 10 time every minutes). It would then be fed to the 
	smart home automation system controlling the devices connected
	to it.
    
	This can result in immense power savings and cost reductions in
	monthly utility bills of which lighting costs contribute to a major
	part. Extending further, a wireless sensor of the mobile device can
	be used to track the user localization throughout the place and this
	can be used to precisely control the appliances of a specific room 
	based on the user locality.

  Working Status: 

    Ambience control using the ambient light sensor was implemented
    Also, broad area wifi localization was implemented as an extension 
    to the basic Functionality in the abstract.

  Directories & Files included:

    - AuraSync : Android app, Checked in into bitbucket.
    - devices-controller : A node js hub/comtroller and places simulator.
    - aurasync.apk :  A apk for AuraSync app. 
                      - Uses 'http://192.168.2.5:3000/api/data' as api. 
                      - Read below for further for details.

  Instructions : 

    This app was built to run on Android version lolipop and up.
    It uses an simulation for lights in different palces
    and was built as a node js application with 4 wifi 
    networks to represent 4 different places. They are namely 
      - welcome2bing, busecure, CODM & CallOfDuty.

    Also, the nodejs app serves as an api for the controller/hub. 
    To run the app simply do 'npm install' in the devices-controller
    directory and then run 'npm run start'.

    The app and the simulator communicates with hub over a data api using 
    http requests. They use API_URL which is currently set to
    'http://192.168.2.5:3000/api/data' for app and simulator loops back
    to its own host for data api. 
    This was not made user editable on UI in android app as ideally api endpoint 
    should never change with time. This would be something fixed when the app 
    is deployed to production and would contain a proper domain name. For 
    test purposes we use the ip address for the server host.

    So, basically to get everything running you would need to replace the 
    API_URL in andriod app, rebuild the apk and also would need to access the 
    simulation using the ip address that the server is running. 

    Note: Do not access the simulator using http://localhost:port in browser 
    after getting the server up locally. The scripts that simulator uses,
    needs same origin access & because of browsers CORS policy, would not 
    work as intended. Instead use the full ip address to access the simulator 
    with port number. 





