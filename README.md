# Eksamen PGR301

## For å kjøre applikasjonen gjør man som følger:
* 1. Kjører denne kommandoen i rot-mappen til prosjektet via terminal:
```
docker run --rm -d --name influxdb \
  -p 8083:8083 -p 8086:8086 -p 25826:25826/udp \
  -v $PWD/influxdb:/var/lib/influxdb \
  -v $PWD/influxdb.conf:/etc/influxdb/influxdb.conf:ro \
  -v $PWD/types.db:/usr/share/collectd/types.db:ro \
  influxdb:1.0
```
* 2. Kjører denne kommandoen i terminal fra samme mappen:
```
mvn spring-boot:run
```
* 3. Kjører denne kommandoen i terminal:
```
docker run -d -p 3000:3000 --name grafana grafana/grafana:6.5.0
```
* 4. Gå til:
```
For InfluxDB:
http://localhost:8083/

For Grafana (Grafana må settes opp, da det ikke følger med noen config):
http://localhost:3000/
```
* 5. Gjør en AWS configure med secrets fra AWS.
* 6. Setter opp GitHub-repo med secrets fra AWS, som forklares mer nøye senere i oppgaven. 
* 7. For å teste AWS-funksjonalitet, kjør en push mot main og/eller gjør en endring og push til en ny branch som merges via pull request til main. Det ligger også en test i koden som kan breakes enkelt, dersom man ønsker å teste hvordan håndtering av feilende tester fungerer.

## Oppgave - DevOps

Med DevOps som arbeidsmåte i tankene- Hvilke forbedringer kan teamet gjøre med fokus på måten de jobber med kildekode og versjonskontroll?

* Når man skal bruke DevOps som arbeidsmetode hvor CI og CD (Continious Integration og Continious Deployment/Delivery) er viktige elementer er det lurt å bruke Github Actions, Travis CI, Jenkins eller andre.
I denne oppgaven brukes det GitHub Actions hvor man via en eller flere yaml/yml-filer i repositoryet under mappen ".github/workflows" er med på å sette opp GitHub Actions, slik at man selv kan tilpasse hvilke tester som skal kjøres under hvilke omstendigheter. Det vil i dette tilfellet settes opp for både Docker, applikasjonen og terraform. Dersom noen av disse "feiler" sitt bygg, vil man ikke få mreget pull-requesten sin til main branch. Dette vil da kunne stoppe em fra å pushe ett brekt bygg eller en test som ikke kjører til main-branch. Den kjører her på både push direkte til main og på pull-requests, men temet bør endre praksis fra å pushe rett til main, og over til å bruke branches for utvikling, deployment og feilrettinger og deretter blir det gjort klar for Release.

* Inne på GitHub repoet, under Settings -> Branches -> Add Rule kan man legge til at både statussjekker må vere OK før man merger som kalles "branch protection", her kan man også legge inn "Require pull request reviews before merging" dersom man er flere utviklere som jobber på samme prosjekt. Ved å aktivere "require approvals" for pull requests må et annet medlem/ en annen som jobber på samme repo eller har tilgang til repoet se over koden og godkjenne pull requesten, før du får merget pull requesten til main branch.
![image](https://user-images.githubusercontent.com/56038804/143770751-5d586819-3eb9-453e-bf7f-151a523383bf.png)

* Som nevnt tildigere er CI og CD grunnelggende prinsipper for DevOps.
Det bør være satt opp slik at hver enkelt utvikler kan jobbe med sitt område og pushe oppdateringer hyppig ved bruk av dev/feature branches. Ofte kan det anbefales å jobbe i form a v ping-pong programmering, som vil si at to utviklere pusher commits til samme branch, under utviklingsprosessen, for å gi økt trygghet, effektivitet og bedre kode. Disse branchene for features bør være kortlevde, og man bør opprette nye dersom man skal implementere noe nytt som er utenfor "scopet" for branchen når den opprettes. Det samme gjelder feilrettinger i koden, forberdinger og bug fixes.
Dette bør gjøres på egne branches, for så å merges inn mot main ofte (men ikke rett in mot release-branch). Dersom de skal merges bør det være en annen som ser over pull-requesten før den blir som tidligere nevnt via branch protection, men også at man har automatiserte tester og automatiserte CI/CD-verktøy som ser at koden bygger hver gang før merge. Automatisering av dette vil kunne sikkre god flyt og effektivitet blant de ulike utviklerene på teamet.
Denne typen utvikling med kortlevde branches for features og fixes vil ofte kalles "Trunk Based Development".

* I Jens sitt forsvar, som kanskje var uteksaminert innen IT for mange år tilbake, og som kanskje ikke har vært helt oppdatert i hvordan utviklingen i hvordan man jobber som team innen IT, moderniseringen og trender kan dette kanskje ha virket "i overkant mye", men det er en god praksis dersom man ønsker å etterstrebe DevOps-prinsipper for flow og det er spesielt viktig at ikke koden i main/deployment/release ikke skal brekke eller inneholder feil som kunne ha vært unngått ved å gjøre det på en måte hvor det hadde vært oppdaget ved hjelp av enkle verktøy. Det skal heller aldri pushes noe direkte til release-branch. Da mister man kontroll og oversikt og risikoen for feil og problemer økes eksponensielt samtidig som det vil være veldig vanskelig å holde styr på relase og nedetiden på applikasjonen vil kunne være langt lengre enn det den er hvis man bruker en god pipeline, med verktøy som hjelper til med å ha trygge og direkte oppdateringer selv på release-branch, med minimale nedetider.
Det er også ekstremt tidsbesparende over tid å få autiomatisert det som kan automatiseres og som bør automatiseres, for at utviklerne kan holde seg til det de faktisk kan best og er spesialisert innen.
Det er som de fleste rullebånd i masseproduksjon. Jo mindre manuelt arbeide, jo flere timer og penger blir spart og jo mindre "fallgruver" oppstår, siden roboter ofte gjør langt færre feil, kan jobbe omtrent 24/7 og krever hverken lunsj pause eller lønnsforhandlinger hvert andre år.

* Risiko og eierskap. Jo flere ledd, jo større er risikoen for at ting går galt, at kunnskap og eierskap går tapt og at man blir sittende fast i et "blamestorming-stadie". Dette er problematisk slik de holder på per dags dato og dette passer ikke med DevOps-prinsipper.
Innen DevOps så er infrastruktur som kode viktig. Dette vil kunne føre til lavere risiko for feil, dersom man reduserer mengden manuelt og ofte repetetivt arbeide, med infrastruktur som kode. Med infrastruktur som kode og CI/CD-verktøy, vil man kunne la verktøyene ta seg av mesteparten av endringene i koden, og man kan gjøre små endringer og feilrettinger underveis, på separate, kortlevde branches, som aktivt merges mot main, for å endre bugs eller legge til features i koden, uten å gjøre noe med infrastrukturen eller større ingrep i kodebasen. Dette minsker antall feil betydelig og gjør det mye lettere for et team å jobbe på samme prosjekt med flere aktive oppgaver som løses samtidig i ulike grupper, uten at dette ødelegger for andre, eller skaper waste.
Det at de som jobber på et prosjekt har litt "skin in the game" er viktig. Det er viktig at de føler et eierskap og ansvar for at produktet skal levere og bli bra, og ikke bare bli stående å peke på andre, når man gir fra seg hele "løsningen/produktet" til et annet team og så tenke "det er deres problem nå". Det er ikke et DevOps mindset, og her bør det gjøres større ingrep, dersom Jens vil lykkes i det å etterstrebe DevOps-prinsippene for banken.

* Som nevnt tidligere så er manuell testing tidkrevende og lite effelktivt sammenlignet med alternativene vi har i dag. Det at de ikke har tester i det heletatt før de sender koden fra seg til Team Dino kan gjøre at det kan gå mye tid til "Waste" ved at beskjeder for bug-fixes og endringer må sendes frem og tilbake mellom utviklere og Team Dino, noe som skaper Waste i form av tid, venting og potensielt treg kommunikasjon.
Det å skrive tester bør ofte gjøres underveis i utviklingsprosessen, mens det å kjøre selve testingen bør automatiseres og legges inn i ifrastruktur, ved hjelp av verktøy for CI/CD.

* Slik det er per dags dato er det vanskelig å forsvare arbeidsmetodikken til banken, da de har ti ganger så mange annsatte i test og drift under kallenavnet "Team Dino" ironisk nok, men de mangler komplett struktur for å holde kommunikasjon mellom utviklere og testere. Slik det virker som de holder på nå, er disse helt separate fra hverandre og mesteparten av intra-kommunikasjonen går tapt eller lyder omtrent som dette "Det har ikke vi gjort, så det er ikke vår feil...". Dette er et tegn på helt fundamentelle feil i bedriftens struktur. For å kunne jobbe effektivt med dette er det bedre med flere små team, som har hyppig kommunikasjon med hverandre, men som også har forståelse for hverandres arbeidsoppgaver og prøver å legge til rette for at de kan ha en mer feller arbeidsmetodikk og progresjon.
 
* Slik det er i dag, med 100 personer på drift og testing, 10 utviklere og kommunikasjonen går via den hellige Tech Lead "Jens", som bedriften har gjort deg helt hundre prosent avhengige av, slik de oppererer per dags dato, må de restrukturere det meste for å etterstrebe DevOps-prinsippene for Flow. Det skal også her sies at de ikke har noe serlig Feedback heller, så det bør jobbes noe med den delen også, slik at ikke "svenskeknappen" etter behov og/eller hver kveld er en varig løsning, som på ingen måte er forutsigbar, gunstig eller bærekraftig.
Det er ikke nødvendigvis feil å ha dedikerte folk til testing, men testene må integreres i infrastrukturen til koden, så man vet at alt som skal testes blir testet før deployment. Utviklere trenger å vite at koden dems kjører riktig, testere trenger å vite at alt som bør testes, blir testet og drift trenger en stabil løsning, som kjører slik den skal slik at kundene til banken er fornøyde.

* Det kan virke som om banken er i en prosess hvor de går fra en mer monolitisk tilnærming til utvikling og over til mikrotjenester i fremtiden og det blir sett på som en fremtidsrettet og mer kontrollert måte å drive på. Veien dit kan være lang, men kontrollen man får over applikasjonen og hvor fremtidsrettet dette er vil kunne være gunstig dersom banken satser på å ha et relativt stort antall brukere. Det skal nevnes at det fort kan være behov for enda fler ansatte i fremtiden for banken, men det er utenfor "scopet" til denne oppgaven.


## Oppgave - Feedback

* Hvilke spørring(er) kan sensor gjøre mot InfluxDB for å analysere problemet?

```sql

For å se backend exception:
SELECT * FROM backend_exception WHERE value = 1

For å se successful transfers:
SELECT * FROM successful_transfer WHERE value = 1

For å se response time:
SELECT mean FROM timed_transfer WHERE mean != 0

```

* Start Grafana på lokal maskin ved hjelp av Docker. Bruk InfluxDB som en datakilde og legg ved et skjermbilde av et Dashboard du har laget som viser en Metric fra InfluxDB som er produsert av Micrometer rammeverket.

* Skjermbilde fra Grafana viser hvordan jeg har tre paneler som henter ut ulike metrics via micrometer og InfluxDB for å hente ut data for backend exceptions, succsessful transfer og response time for requests mot transfer.

* respons tid for account transfer via "@Timed"-annotasjonen som jeg har kalt timed_transfer i koden.

* for successful transfer og for backend exception er det brukt en try-catch i transfer for å se om den gikk gjennom eller ikke.

![image](https://user-images.githubusercontent.com/56038804/144093435-3c64a0e3-9586-4fae-9367-d55521cefd51.png)


## Oppgave Terraform

* Grunnen til at terraformkoden fungerte første gangen den ble kjørt er at Jens sin terraform state var lagret lokalt og dette er terraform helt avhengig av å kunne akksessere for å gjøre terraform plan og endringer i infrastrukturen. Før terraform kjører en opperasjon, vil terraform oppdatere .tfstate-filen med infrastruktur fra koden. Denne .tfstate-filen som ingen ender opp med å ha, resulterer i at terraform ikke har noen lagret status over infrastruktur og konfigurasjon. Dette gjør at Terraform ikke kan mappe ressurser for applikasjonen mot terraform-konfigurasjonen.
De andre når de andre utviklerene prøver å jobbe på prosjektet og kjører denne i etterkant uten state-filen vil den prøve å opprette en ny state-fil og sende den til AWS S3-bucketen på nytt, men får beskjed om at det finnes fra før, så det vil ikke fungere da det allerede ligger en S3 bucket under det samme navnet i AWS.
Når man jobber i team er det viktig at denne state-filen lagres ekstert, slik at alle på teamet kan akksessere denne filen og "pushe" oppdateringer til state, når det blir gjort endringer.

### AWS CLI

Sensor ønsker å lage sin bucket ved hjelp av CLI. Sensor har aws kommandolinje installert på sin lokale maskin. Hva må sensor gjøre for å konfigurere AWS nøkler/Credentials? Anta at Sensor sin AWS bruker ikke har nøkler/credentials fra før.

* For å gjøre en aws configure må man ha en AWS Access Key ID og en AWS Secret Access Key. Disse kan man opprette i sin IAM-bruker på AWS. Inne på IAM (Identity and Access Management) Trykker man på "My Secyrity Credentials" hvor man så får muligheten til å trykke på "Create access key". 
![image](https://user-images.githubusercontent.com/56038804/143626589-607bf0b3-9933-4888-98a3-d34f0fad2b31.png)
* Dette gjør man og da må man ta vare på disse nøklene, men det er viktig at dette er hemmeligheter som ikke skal legges ut eller deles med andre. Disse får man muligheten til å laste ned som en .csv-fil.
Når man så kjører aws confiugure legger man først inn Access key, så Secret Access Key, region (eu-west-1) og default format (json).
Etter dette er man konfigurert og autentifisert mot sin AWS-bruker og kan kjøre kommandoer fra CLI på vegne av den, slik som det å sette opp en bucket i S3 med kommandoen:

```
aws s3api create-bucket --bucket stwe001 --region eu-west-1 --create-bucket-configuration LocationConstraint=eu-west-1
```

### Endre Terraform provider til å bruke en S3 backend for state.

![image](https://user-images.githubusercontent.com/56038804/143772126-24ddc0c7-71fe-4190-b056-5ffca357f38c.png)

![image](https://user-images.githubusercontent.com/56038804/143772092-573504e7-2e30-4a07-a956-ef5722f8162e.png)

### Terraform kode

### Terraform i Pipeline

* Beskriv hva sensor må gjøre etter han/hun har laget en fork for å få pipeline til å fungere for i sin AWS/gitHub konto.

* Hvilke verdier må endres i koden?

* I koden som ligger i infra -> provider.tf bør sensor gjøre noen endringer hvor h*n erstatter "stwe001" med f.eks sitt brukernavn.

![image](https://user-images.githubusercontent.com/56038804/143681629-ce26550e-7b55-4c50-a7b3-c86ac42086bf.png)

* Dette gjelder også for infra -> ECR.tf 

![image](https://user-images.githubusercontent.com/56038804/143681644-66e2f4ac-558c-4e9f-bc5d-1de91e6dfdd4.png)

* Sensor kan også endre i koden som kjører docker-kommandoer via workflows under .github -> workflows -> docker.yml for å navngi tags på docker-images annerledes

![image](https://user-images.githubusercontent.com/56038804/143681768-28d3a0eb-02f3-469b-a0f0-ce2c0de22b0b.png)


* Hemmeligheter som må legges til under GitHub secrets for at man skal kunne autenifiseres i forbindelse med AWS er  Access key og secret access key. Disse legges inn i GitHub secrets på følgende måte. Inne på repo: Settings -> Secrets -> New repository secret. Det er viktig å navngi de slik det er vist på bildet, for at det skal fungere med koden.
![image](https://user-images.githubusercontent.com/56038804/143626395-02a9ff3a-1d79-4686-bca9-42b9c2f6638e.png)


### Dockerfile

NB. Der det står *tag name* kan man bruke en identifikator for å referere til imaget. Man kan fint bruke f.eks " *brukernavn*-image " eller lignende.

Hva vil kommandolinje for å _bygge_ et container image være?

* For å Bygge Docker image bruker man kommandoen:
```
docker build -t *tag name*
```

Hva vil kommando for å _starte_ en container være? Applikasjonen skal lytte på port 7777 på din maskin.

* For å starte en container på maskinen kjører man denne kommandoen der 7777:80 er port-mapping:
```
docker run -p 7777:80 *tag name*
```

Medlemmer av "Team Dino" har av og til behov for å kjøre to ulike versjoner av applikasjonen lokalt på maskinen sin, _samtidig_ .Hvordan kan de gjøre dette uten å få en port-konflikt?  Hvilke to kommandoer kan man kjøre for å starte samme applikasjon to ganger, hvor den ene bruker port 7777 og den andre 8888?

For å kjøre to instanser av samme container-image på to ulike porter vil man bruke port-mapping til å mappe de til hver sin port på maskinen, men samme port i container-image, så ikke noen funksjonalitet går tapt.

```
docker run -p 7777:80 *tag name*
```
```
docker run -p 8888:80 *tag name*
```
