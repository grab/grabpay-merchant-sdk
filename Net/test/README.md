#### How to get test coverage:

1. Run this command:
```dotnet test /p:CollectCoverage=true /p:CoverletOutputFormat=cobertura```

2. Check that file `coverage.cobertura.xml` exist in NetTest project.

3. Run this command:
```dotnet ~/.nuget/packages/reportgenerator/5.0.0/tools/net5.0/ReportGenerator.dll -reports:"./coverage.cobertura.xml" -targetdir:"./coverage" -reporttypes:Html```
