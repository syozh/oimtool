������ ������: oimtool.cmd <command> [args]

�������:

  1) itresparams <itresname> - ����� ���������� IT-�������.

  2) categories - ����� �������������� ��������� ��������.
     
  3) export1 [args]
     - ������� �������� ��������. ������ ������������ ������� "Category:Name". 
       ��������� ��������� � ����� categories.txt.
       ������ ������������ �������: "Lookup:Lookup.SAPHCM.OM.ReconAttrMap".
       ��� ������� ��������� � ���� deployment-����.

     export2 [args]
     - ������ ������ ��������� � ��������� ����.
   
  4) import [args] - ������ �������� deployment-������. (!!!) ���������� ������������ ���� �� ��������.

  5) register [args] - ����������� ��������. ����������� zip-������ ��������.

  6) unregister [args] - �������� ��������. ������ ������������ ������� "PluginClassName:Version".

  7) purge [args] - Purge�ache ��� �������� ��������.

  8) purgeall - PurgeCache All

�������� ������������ ����������.
  - � ���� ������ � ��������� ������:  oimtool <command> arg1 arg2 ...
  - �� ������� �����, ����� -f:        oimtool <command> -f <file>
    ������ ������ � ������, ������������ � ������� '#', ������������.
