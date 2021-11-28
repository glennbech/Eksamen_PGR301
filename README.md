# Eksamen PGR301

## Oppgave - DevOps

Med DevOps som arbeidsmåte i tankene- Hvilke forbedringer kan teamet gjøre med fokus på måten de jobber med kildekode og versjonskontroll?

* Når man skal bruke DevOps som arbeidsmetode hvor CI og CD er viktige elementer er det lurt å bruke Github Actions, Travis CI, Jenkins eller andre.
I denne oppgaven brukes det GitHub Actions hvor man via en eller flere yaml/yml-filer i repositoryet under mappen ".github/workflows" er med på å sette opp GitHub Actions, slik at man selv kan tilpasse hvilke tester som skal kjøres under hvilke omstendigheter. Det vil i dette tilfellet settes opp for både Docker, applikasjonen og terraform. Dersom noen av disse "feiler" sitt bygg, vil man ikke få mreget pull-requesten sin til main branch. Dette vil da kunne stoppe em fra å pushe ett brekt bygg eller en test som ikke kjører til main-branch. Den kjører her på både push direkte til main og på pull-requests, men temet bør endre praksis fra å pushe rett til main, og over til å bruke branches for utvikling, deployment og feilrettinger og deretter blir det gjort klar for Release.

* Inne på GitHub repoet, under Settings -> Branches -> Add Rule kan man legge til at både statussjekker må vere OK før man merger som kalles "branch protection", her kan man også legge inn "Require pull request reviews before merging" dersom man er flere på samme prosjekt. Det fører til at en annen må se over koden og godkjenne, før du får merget pull requesten til main branch.
![image](https://user-images.githubusercontent.com/56038804/143770751-5d586819-3eb9-453e-bf7f-151a523383bf.png)

* Som nevnt tildigere er CI og CD (Continious Integration og Continious Deployment/Delivery) grunnelggende prinsipper for DevOps.
Det bør være satt opp slik at hver enkelt utvikler kan jobbe med sitt område og pushe oppdateringer hyppig ved bruk av dev/feature branches. Disse bør være kortlevde og man bør opprette nye, dersom man skal implementere noe nytt. Det samme gjelder feilrettinger i koden eller forbedringer. Dette bør gjøres på egne branches, for så å merges inn mot main ofte. Dersom de skal merges bør det være en annen som ser over pull-requesten før den blir merget, men også at man har automatiserte teste og ser at koden bygger hver gang før merge. Dette bør da som sagt være automatisert for å sikkre god flyt og effektivitet blant de ulike utviklerene på teamet.

* I noen tilfeller kan dette virke "i overkant", men det er en god praksis dersom man ønsker å etterstrebe DevOps-prinsipper for flow og det er spesielt viktig at ikke koden i main og/ eller deployment brekker eller inneholder feil som kunne ha vært unngått ved å gjøre det ordentlig i første omgang. Det skal heller aldri pushes noe direkte til release-branch. Da mister man kontroll og oversikt og risikoen for feil og problemer økes eksponensielt.
Det er også tidsbesparende over tid å få autiomatisert det som kan automatiseres, for at utviklerne kan holde seg til det de faktisk kan best og er spesialisert innen.

- Risikooverføring
- Manuelle tester
- Waste (Venting, Delvis fullført, manuelle prosesser, dersom noe går galt og team dino må si ifra til dev-team for å rette opp i det)
- Det å skrive tester bør ofte gjøres underveis, mens selve testingen bør automatiseres. Det bør derfor ikke være slik at et felles team for test og drift har ti ganger flere ansatte enn de som jobber med utvikling. Her bør de spre ressursene mer. Det vil være mye tid og ressurser som går bort i at teamene sender filer og meldinger frem og tilbake fordi ting ikke fungerer og kommunikasjon her er et bristepunkt, da de bare driver "blamestorming". Det er tydelig at teamene ikke har forståelse for hverandres ferdigheter og arbeidsområder.
- Færrest muliug overleveringer for å slippe "wall of confusion"
SkalBank har bestemt seg for å bruke DevOps som underliggende prinsipp for all systemutvikling i banken. Er fordeling av oppgaver mellom API-teamet og "Team Dino" problematisk med dette som utgangspunkt? Hvilke prinsipper er det som ikke etterleves her? Hva er i så fall konsekvensen av dette?

## Oppgave - Feedback

* Hvilke spørring(er) kan sensor gjøre mot InfluxDB for å analysere problemet? For eksempel noe i retning av;

```sql
select * from my_timer_metric_name
```

* Start Grafana på lokal maskin ved hjelp av Docker. Bruk InfluxDB som en datakilde og legg ved et skjermbilde av et Dashboard du har laget som viser en Metric fra InfluxDB som er produsert av Micrometer rammeverket.

## Oppgave Terraform

* Grunnen til at terraformkoden fungerte første gangen den ble kjørt er at Jens sin terraform state var lagret lokalt og dette er terraform helt avhengig av å kunne akksessere for å gjøre terraform plan og endringer i infrastrukturen. Før terraform kjører en opperasjon, vil terraform oppdatere .tfstate-filen med infrastruktur fra koden. Denne .tfstate-filen som ingen ender opp med å ha, resulterer i at terraform ikke har noen lagret status over infrastruktur og konfigurasjon. Dette gjør at Terraform ikke kan mappe ressurser for applikasjonen mot terraform-konfigurasjonen.
Når man jobber i team er det viktig at denne filen lagres ekstert, slik at alle på teamet kan akksessere denne filen og "pushe" oppdateringer til state, når det blir gjort endringer.

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

AWS brukeren du har fått utlevert har ingen nøkler. Ved hjelp av Console (UI) Lag en Access Key som du kan bruke videre i oppgaven.

### Endre Terraform provider til å bruke en S3 backend for state.

![image](https://user-images.githubusercontent.com/56038804/143772126-24ddc0c7-71fe-4190-b056-5ffca357f38c.png)

![image](https://user-images.githubusercontent.com/56038804/143772092-573504e7-2e30-4a07-a956-ef5722f8162e.png)

### Terraform kode

### Terraform i Pipeline

* Beskriv hva sensor må gjøre etter han/hun har laget en fork for å få pipeline til å fungere for i sin AWS/gitHub konto.

* Hvilke verdier må endres i koden?

*I koden som ligger i infra -> provider.tf bør sensor gjøre noen endringer hvor h*n erstatter "stwe001" med f.eks sitt brukernavn.

![image](https://user-images.githubusercontent.com/56038804/143681629-ce26550e-7b55-4c50-a7b3-c86ac42086bf.png)

*Dette gjelder også for infra -> ECR.tf 

![image](https://user-images.githubusercontent.com/56038804/143681644-66e2f4ac-558c-4e9f-bc5d-1de91e6dfdd4.png)

*Sensor kan også endre i koden som kjører docker-kommandoer via workflows under .github -> workflows -> docker.yml for å navngi tags på docker-images annerledes

![image](https://user-images.githubusercontent.com/56038804/143681768-28d3a0eb-02f3-469b-a0f0-ce2c0de22b0b.png)


* Hemmeligheter som må legges til under GitHub secrets for at man skal kunne autenifiseres i forbindelse med AWS er  Access key og secret access key. Disse legges inn i GitHub secrets på følgende måte. Inne på repo: Settings -> Secrets -> New repository secret. Det er viktig å navngi de slik det er vist på bildet, for at det skal fungere med koden.
![image](https://user-images.githubusercontent.com/56038804/143626395-02a9ff3a-1d79-4686-bca9-42b9c2f6638e.png)


### Dockerfile

NB. Der det står *tag name* kan man bruke en identifikator for å referere til imaget. Man kan fint bruke f.eks " *brukernavn*-image " eller lignende.

Hva vil kommandolinje for å _bygge_ et container image være?
```
docker build -t *tag name*
```

Hva vil kommando for å _starte_ en container være? Applikasjonen skal lytte på port 7777 på din maskin.

```
docker run -p 7777:80 *tag name*
```

Medlemmer av "Team Dino" har av og til behov for å kjøre to ulike versjoner av applikasjonen lokalt på maskinen sin, _samtidig_ .Hvordan kan de gjøre dette uten å få en port-konflikt?  Hvilke to kommandoer kan man kjøre for å starte samme applikasjon to ganger, hvor den ene bruker port 7777 og den andre 8888?

```
docker run -p 7777:80 *tag name*
```
```
docker run -p 8888:80 *tag name*
```
