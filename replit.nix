{ pkgs }: {
  deps = [
    pkgs.dotnet-sdk
    pkgs.openjdk16
     pkgs.dafny
    pkgs.python38
    pkgs.python38Packages.numpy
  ];
}
