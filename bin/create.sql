使用PL/SQL建表
begin
for i in 1..31 loop
execute immediate
'create table e_detail_'|| to_char(i)||
'(
name varchar2(20),
srcId varchar2(5),
dstId varchar2(5),
sersorAddress varchar2(7),
count number(2),
cmd varchar2(5),
status number(2),
data number(9,4),
gather_date date
)';
end loop;
end;
/

使用PL/SQL删除表
　　BEGIN
　　FOR i IN 1..31 LOOP
　　EXECUTE IMMEDIATE
　　'DROP TABLE t_detail_'||TO_CHAR(i);
　　END LOOP;
　　END;
　　/
