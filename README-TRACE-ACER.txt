
Bookmarked Trace

    MSFT
        cosmos: 
            ucare-09-river.cs.uchicago.edu:8080?bookmark=f2c2b04f-83b4-4b64-b052-4f48f8808ac9.json
        azure storage: 
            ucare-09-river.cs.uchicago.edu:8080?bookmark=aa4a10a6-0873-47e1-8ce4-3a5d4c6e54fd.json
        bing index: 
            ucare-09-river.cs.uchicago.edu:8080?bookmark=601980bb-e753-4201-874e-c318cfa4dfb1.json
        bing selection: 
            ucare-09-river.cs.uchicago.edu:8080?bookmark=aeb3e165-b8a3-417f-933f-d8edffe41e61.json





-----------------------------------------------------------------------------
                                INSTALLATION


Clone repo

    cd ~
    git clone -b traceViz https://github.com/daniarherikurniawan/hillview.git
    cd hillview/
    git branch


Install Dependencies
    

        cd ~/hillview/bin 

        INSTALL_CASSANDRA=0
        SAVEDIR=$PWD
        mydir="$(dirname "$0")"
        if [[ ! -d "$mydir" ]]; then mydir="$PWD"; fi
        source ${mydir}/lib.sh

        echo "Installing programs needed to build"

        case "$OSTYPE" in
            linux*)
            # Location where node.js version 11 resides.
                echo "Installing curl"
                ${SUDO} ${INSTALL} install curl -y
            curl -sL https://deb.nodesource.com/setup_12.x | ${SUDO} -E bash -
        esac

        ${SUDO} ${INSTALL} install wget maven ${NODEJS} ${NPM} ${LIBFORTRAN} unzip gzip
        echo "Installing typescript compiler"
        ${SUDO} npm install -g typescript@3.9

        pushd ..
        if [ ! -d apache-tomcat-9.0.4 ]; then
            echo "Installing apache Tomcat web server"
            wget http://archive.apache.org/dist/tomcat/tomcat-9/v9.0.4/bin/apache-tomcat-9.0.4.tar.gz
            tar xvfz apache-tomcat-9.0.4.tar.gz
            cd apache-tomcat-9.0.4/webapps
            rm -rf ROOT* examples docs manager host-manager
            ln -s ../../web/target/web-1.0-SNAPSHOT.war ROOT.war
            cd ../..
            rm -rf apache-tomcat-9.0.4.tar.gz
        else
            echo "Tomcat already installed"
        fi
        popd

        echo "Downloading test data"
        cd ${mydir}/../data/ontime
        ./download.py
        cd ../ontime_private
        ./gen_metadata.py
        cd ../..

        cd web/src/main/webapp
        echo "Installing Javascript packages"
        rm -f node_modules/typescript
        npm install
        npm link typescript
        cd ${SAVEDIR}

        if [ ${INSTALL_CASSANDRA} -eq 1 ]; then
            ./${mydir}/install-cassandra.sh
        fi

Install & Compile  

    cd ~/hillview/cassandra-shaded/    
    mvn install

    cd ~/hillview/
    bin/rebuild.sh


Running LOCALLY on ONE node

    # make sure it works!!

    lsof -i:8080
    lsof -i:3569
    sudo lsof -i -P -n | grep LISTEN

    # kill -9 $(pgrep -f java)
    pgrep -f java

    cd ~/hillview
    bin/backend-start.sh &!
    bin/frontend-start.sh &!

kill    
    kill -9 <anything under jps>




