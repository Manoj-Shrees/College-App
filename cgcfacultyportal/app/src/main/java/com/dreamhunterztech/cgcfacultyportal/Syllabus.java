package com.dreamhunterztech.cgcfacultyportal;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class Syllabus extends Fragment {

    CardView engineeringcard,compappcard,managementcard,hmanagementcard,pharmacard,biotechcard,educard,subcode;
    ImageView pannelclosebutton;
    Button opensubcodebtn;
    String subname [] ={"Choose Branch","Btech( 1st year )","Btech-ME","Btech-ECE","Btech-CSE","Btech-IT","BBA","MBA","BCA","MCA","B.Pharmacy","M. Pharm. (PHARMACEUTICS)","B.Sc.Biotechnology","M.Sc.Biotechnology","HMCT","B.Sc.ATHM"};
    ArrayAdapter adapter;
    Spinner subcodeselection;
    CardView  btcse,btece,btit,btme,bt1styr,bca,mca,bba,mba,hmct,athm,bpharma,mpharma,bscbiotech,mscbiotech,bed;
    Dialog d;
    private Intent openpdf;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.syallabuslayout,container,false);
        engineeringcard = (CardView) view.findViewById(R.id.engineering);
        compappcard = (CardView) view.findViewById(R.id.compapp);
        managementcard = (CardView) view.findViewById(R.id.management);
        hmanagementcard = (CardView) view.findViewById(R.id.Hmangement);
        pharmacard = (CardView) view.findViewById(R.id.pharmacy);
        biotechcard = (CardView) view.findViewById(R.id.Biotech);
        educard = (CardView) view.findViewById(R.id.edu);
        subcode = (CardView) view.findViewById(R.id.subcode);
        createdialog();

        engineeringcard.setCardBackgroundColor(Color.parseColor("#b67deb"));
        compappcard.setCardBackgroundColor(Color.parseColor("#52a66a"));
        managementcard.setCardBackgroundColor(Color.parseColor("#cf2cf0e2"));
        hmanagementcard.setCardBackgroundColor(Color.parseColor("#d9d918"));
        pharmacard.setCardBackgroundColor(Color.parseColor("#59c8d3"));
        biotechcard.setCardBackgroundColor(Color.parseColor("#675fdb"));
        educard.setCardBackgroundColor(Color.parseColor("#b3f45ef4"));
        subcode.setCardBackgroundColor(Color.parseColor("#b308e3a5"));
        engineeringcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.setContentView(R.layout.syllabusenglayout);
                btcse = (CardView) d.findViewById(R.id.pdfCSE);
                btece = (CardView) d.findViewById(R.id.pdfECE);
                btit = (CardView) d.findViewById(R.id.pdfIT);
                btme = (CardView) d.findViewById(R.id.pdfME);
                bt1styr = (CardView) d.findViewById(R.id.pdf1styr);
                pannelclosebutton = (ImageView) d.findViewById(R.id.engpannelclose);
                pannelclosebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });


                btcse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/CSE/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });

                btece.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/ECE/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });

                btit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/IT/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });

                btme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/ME/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });

                bt1styr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/B.Tech%201st%20Year/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });

                d.show();
            }
        });

        compappcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.setContentView(R.layout.syllabuscomapplayout);
                bca = (CardView) d.findViewById(R.id.pdfbca);
                mca = (CardView) d.findViewById(R.id.pdfmca);
                pannelclosebutton = (ImageView) d.findViewById(R.id.comapppannelclose);
                pannelclosebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                bca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/BCA/index.html?page=1");
                                getContext().startActivity(openpdf);
                    }
                });

                mca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/MCA/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });
                d.show();
            }
        });

        managementcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.setContentView(R.layout.syllabusmagementlayout);
                pannelclosebutton = (ImageView) d.findViewById(R.id.managpannelclose);
                bba = (CardView) d.findViewById(R.id.pdfbba);
                mba = (CardView) d.findViewById(R.id.pdfmba);
                pannelclosebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                bba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/BBA/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });

                mba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/MBA/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });
                d.show();
            }
        });

        hmanagementcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.setContentView(R.layout.syllabushmlayout);
                pannelclosebutton = (ImageView) d.findViewById(R.id.hmpannelclose);
                hmct = (CardView) d.findViewById(R.id.pdfhmct);
                athm = (CardView) d.findViewById(R.id.pdfathm);
                pannelclosebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                hmct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/HMCT/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });
                athm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/B.Sc%20ATHM/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });

                d.show();
            }
        });

        pharmacard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.setContentView(R.layout.syllabuspharmalayout);
                pannelclosebutton = (ImageView) d.findViewById(R.id.pharmapannelclose);
                bpharma = (CardView) d.findViewById(R.id.pdfbpharma);
                mpharma = (CardView) d.findViewById(R.id.pdfmpharma);
                pannelclosebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                bpharma.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/B.Pharmacy/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });
                mpharma.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/M.Pharm(PHARMACEUTICS)/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });
                d.show();
            }
        });

        biotechcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.setContentView(R.layout.syllabusbiotechlayout);
                pannelclosebutton = (ImageView) d.findViewById(R.id.biotechpannelclose);
                bscbiotech = (CardView) d.findViewById(R.id.pdfbscbiotech);
                mscbiotech = (CardView) d.findViewById(R.id.pdfmscbiotech);
                pannelclosebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                bscbiotech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/B.Sc%20Biotechnology/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });
                mscbiotech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Syllabus/M.Sc%20Biotechnology/index.html?page=1");
                        getContext().startActivity(openpdf);
                    }
                });
                d.show();
            }
        });

        educard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.setContentView(R.layout.syllabusedulayout);
                pannelclosebutton = (ImageView) d.findViewById(R.id.edupannelclose);
                bed = (CardView) d.findViewById(R.id.pdfbed);
                pannelclosebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                bed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*openpdf = new Intent(getContext(),PDFviewer.class);
                        openpdf.putExtra("pdf_url","http://www.ptu.ac.in/userfiles/file/syllabus/bparmacy/2013/M_Pharm/M_%20Pharm_%20(PHARMACEUTICS)_2013_uploaded_02_07_13.pdf");
                        getContext().startActivity(openpdf);*/
                        Toast.makeText(getContext(),"sorry no data avialable we will provide it soon thankyou!!!",Toast.LENGTH_SHORT).show();
                    }
                });
                d.show();
            }
        });


        subcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.setContentView(R.layout.subcodedialog);
                pannelclosebutton = (ImageView) d.findViewById(R.id.subcodepannelclose);
                opensubcodebtn = (Button) d.findViewById(R.id.opensubcode);
                adapter =  new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,subname);
                subcodeselection = (Spinner) d.findViewById(R.id.subcodeoption);
                subcodeselection.setAdapter(adapter);
                pannelclosebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                opensubcodebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        switch (subcodeselection.getSelectedItemPosition())
                        {
                                case 0:
                                    Toast.makeText(getActivity(),"you have not selected any option",Toast.LENGTH_SHORT).show();
                                break;

                            case 1:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/B_tech_1st_year/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;

                            case 2:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/ME/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;

                            case 3:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/ECE/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;


                            case 4:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/CSE/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;

                            case 5 :
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/IT/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;

                            case 6:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/BBA/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;


                            case 7:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/MBA/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;


                            case 8:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/BCA/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;


                            case 9:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/MCA/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;

                            case 10:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/B_pharmacy/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;


                            case 11:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/M__Pharm___PHARMACEUTICS/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;


                            case 12:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/B_Sc_Biotechnology/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;


                            case 13:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/M_Sc_Biotechnology/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;


                            case 14:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/HMCT/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;

                            case 15:
                                openpdf = new Intent(getContext(),PDFviewer.class);
                                openpdf.putExtra("pdf_url","https://cgcstudentportal.000webhostapp.com/Subjectcode/B_Sc_ATHM/index.html?page=1");
                                getContext().startActivity(openpdf);
                                break;

                        }

                    }
                });
                d.show();
            }
        });

        return view;
    }

    private void createdialog()
    {
        d = new Dialog(getActivity());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setWindowAnimations(R.style.animateddialog);
        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
