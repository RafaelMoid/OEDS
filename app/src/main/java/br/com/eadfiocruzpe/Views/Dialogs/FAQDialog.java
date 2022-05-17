package br.com.eadfiocruzpe.Views.Dialogs;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.ComponentContentExpanderContract;
import br.com.eadfiocruzpe.Contracts.FAQDialogContract;
import br.com.eadfiocruzpe.Views.Components.ComponentContentExpander;

public class FAQDialog extends Dialog {

    // UI
    private FAQDialogContract.View mCallback;
    private ImageView mBtnClose;
    private ArrayList<ComponentContentExpander> content = new ArrayList<>();
    private ComponentContentExpander mContExp1;
    private ComponentContentExpander mContExp2;
    private ComponentContentExpander mContExp3;
    private ComponentContentExpander mContExp4;
    private ComponentContentExpander mContExp5;
    private ComponentContentExpander mContExp6;
    private ComponentContentExpander mContExp7;
    private ComponentContentExpander mContExp8;
    private ComponentContentExpander mContExp9;
    private ComponentContentExpander mContExp10;
    private ComponentContentExpander mContExp11;
    private ComponentContentExpander mContExp12;
    private ComponentContentExpander mContExp13;
    private ComponentContentExpander mContExp14;

    public static HashMap<String, String> expenseNameDescription = new HashMap<String, String>(){{
        put("Atenção Básica", "");
        put("Assistência Hospitalar e Ambulatorial", "");
        put("Suporte Profilático e Terapêutico", "");
        put("Vigilância Sanitária", "");
        put("Vigilância Epidemiológica", "");
        put("Alimentação e Nutrição", "");
        put("Outras Subfunções", "");
    }};

    public FAQDialog(@NonNull Context context, FAQDialogContract.View callback) {
        super(context, R.style.DialogTheme);
        setContentView(R.layout.dialog_faq);

        mCallback = callback;

        initViews(this);
        loadContent();
        initEvents();

        show();
    }

    /**
     * Initialization
     */
    private void initViews(final Dialog dialog) {
        mBtnClose = dialog.findViewById(R.id.dialog_info_btn_close);

        mContExp1 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_app_info_source),
                mContentExpanderListener, "", "");

        mContExp2 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_what_is_siops),
                mContentExpanderListener, "", "");

        mContExp3 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_is_siops_mandatory),
                mContentExpanderListener, "", "");

        mContExp4 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_source_resources_health),
                mContentExpanderListener, "", "");

        mContExp5 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_actions_services_ph),
                mContentExpanderListener, "", "");

        mContExp6 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_actions_services_not_ph),
                mContentExpanderListener, "", "");

        mContExp7 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_how_can_influence_ph),
                mContentExpanderListener, "", "");

        mContExp8 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_what_do_see_irregularity),
                mContentExpanderListener, "", "");

        mContExp9 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_why_cant_search_certain_years),
                mContentExpanderListener, "", "");

        mContExp10 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_where_do_values_section_value_citizen_day_come_from),
                mContentExpanderListener, "", "");

        mContExp11 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_where_do_values_section_ranking_come_from),
                mContentExpanderListener, "", "");

        mContExp12 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_where_do_values_section_investments_ph_come_from),
                mContentExpanderListener, "", "");

        mContExp13 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_where_do_values_section_invested_resources_come_from),
                mContentExpanderListener, "", "");

        mContExp14 = new ComponentContentExpander(
                (FrameLayout) dialog.findViewById(R.id.content_expander_what_are_legal_devices_siops),
                mContentExpanderListener, "", "");
    }

    private void loadContent() {
        mContExp1.setTitle("De onde vem as informações apresentadas pelo aplicativo?");
        mContExp1.setContent(
                "Todas as informações orçamentárias do aplicativo são provenientes do Sistema de Informações sobre Orçamentos Públicos em Saúde (SIOPS).\n\n" +
                "O aplicativo procura manter os valores extraídos do SIOPS consistentes, realizando somente operações que permitam a exibição das informações como elas se encontram em sua origem.\n\n" +
                "Quando necessário, são realizadas classificações e operações simples, que permitem a observação dos dados sob uma perspectiva diferente, possibilitando uma maior compreensão do orçamento relacionado ao sistema público de saúde.");
        mContExp1.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp1.setShowBottomLine(true);

        mContExp2.setTitle("O que é o SIOPS?");
        mContExp2.setContent(
                "O Sistema de Informações sobre Orçamentos Públicos em Saúde (SIOPS) é o sistema de registro eletrônico centralizado das informações de saúde referentes aos orçamentos públicos da União, dos Estados, do Distrito Federal e dos Municípios, que possibilita o monitoramento da aplicação de recursos na saúde.\n\n" +
                "Fonte: http://portalms.saude.gov.br/repasses-financeiros/siops");
        mContExp2.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp2.setShowBottomLine(true);

        mContExp3.setTitle("O SIOPS é obrigatório? Quem alimenta o SIOPS?");
        mContExp3.setContent(
                "Sim. O sistema é obrigatório para a União, Estados, Distrito Federal e Municípios, conforme determina a Lei Complementar nº 141 de 13 de Janeiro de 2012.\n\n" +
                "Os dados são declarados pelos Entes Federados (União, Estados, Distrito Federal e Municípios) de acordo com seu Balanço Geral, com informações de receitas totais e gastos com ações e serviços públicos de saúde (ASPS).\n\n" +
                "Fonte: http://portalms.saude.gov.br/repasses-financeiros/siops");
        mContExp3.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp3.setShowBottomLine(true);

        mContExp4.setTitle("De onde vem  os recursos para a Saúde?");
        mContExp4.setContent(
                "No Brasil, a saúde é financiada por meio do orçamento da União, dos estados, do Distrito Federal e dos municípios, que arrecadam recursos junto à população, através dos impostos e das contribuições sociais, ou seja, são provenientes do bolso de todos os brasileiros.\n\n" +
                "Assim, é importante que os cidadãos saibam onde está sendo investido seus impostos e possa a partir das informações interferir nos processos decisórios, seja, por meio da Participação organizada nos Conselhos de Saúde ou participação em outras esferas de planejamento e decisão política.");
        mContExp4.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp4.setShowBottomLine(true);

        mContExp5.setTitle("Que investimentos são considerados como Ações e Serviços Públicos de Saúde?");
        mContExp5.setContent(
                "Os investimentos referentes a:\n\n" +
                "1. Vigilância em saúde, incluindo a epidemiológica e a sanitária;\n\n" +
                "2. Atenção integral e universal à saúde em todos os níveis de complexidade, incluindo assistência terapêutica e recuperação de deficiências nutricionais;\n\n" +
                "3. Capacitação de pessoal do Sistema Único de saúde;\n\n" +
                "4. Desenvolvimento científico e tecnológico e controle de qualidade, promovidos por instituições do SUS\n\n" +
                "5. Produção, aquisição e distribuição de insumos específicos dos serviços de saúde do SUS, tais como: imunobiológicos, sangue e hemoderivados, medicamentos e equipamentos médico-odontológicos;\n\n" +
                "6. Saneamento básico de domicílios ou de pequenas comunidades, desde que seja aprovado pelo Conselho de Saúde;\n\n" +
                "7. Saneamento básico de distritos sanitários especiais indígenas e de comunidades remanescentes de quilombos;\n\n" +
                "8. Manejo ambiental vinculado diretamente ao controle de vetores de doenças;\n\n" +
                "9. Investimento na rede física do SUS, incluindo obras de recuperação, reformas, ampliação e construção de estabelecimentos públicos de saúde;\n\n" +
                "10. Remuneração de pessoal ativo da área da saúde em atividade, incluído os encargos sociais;\n\n" +
                "11. Ações de apoio administrativo realizadas pelas instituições públicas do SUS;\n\n" +
                "12. Gestão do sistema público de saúde e operações de unidades prestadoras de serviços públicos de saúde. (Art. 3º da LC nº 141/2012).\n\n" +
                "Fonte: http://portalms.saude.gov.br/repasses-financeiros/siops");
        mContExp5.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp5.setShowBottomLine(true);

        mContExp6.setTitle("Que investimentos não são considerados como Ações e Serviços Públicos de Saúde?");
        mContExp6.setContent(
                "1. Pagamento de aposentadorias e pensões, inclusive dos servidores da saúde;\n\n" +
                "2. Pessoal ativo da área de saúde quando em atividade alheia à referida área;\n\n" +
                "3. Assistência à saúde que não atenda ao princípio de acesso universal;\n\n" +
                "4. Merenda escolar e outros programas de alimentação, ainda que executados em unidades do SUS;\n\n" +
                "5. Saneamento básico, inclusive quanto às ações financiadas e mantidas com recursos provenientes de taxas, tarifas ou preços públicos instituídos para essa finalidade;\n\n" +
                "6. Limpeza urbana e remoção de resíduos;\n\n" +
                "7. Preservação e correção do meio ambiente, realizadas pelos órgãos de meio ambiente dos entes da Federação ou por entidades não governamentais;\n\n" +
                "8. Ações de assistência social;\n\n" +
                "9. Obras de infraestrutura, ainda que realizadas para beneficiar direta ou indiretamente a rede de saúde. (Art. 4º da LC nº 141/2012).\n\n" +
                "Fonte: http://portalms.saude.gov.br/repasses-financeiros/siops");
        mContExp6.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp6.setShowBottomLine(true);

        mContExp7.setTitle("Eu posso influenciar na maneira como o governo municipal ou estadual utiliza os investimentos recebidos?");
        mContExp7.setContent(
                "Sim, por meio da participação nos Conselhos de Saúde que aprovam os planos de saúde.\n\n" +
                "Há governos que constroem o orçamento de modo participativo, esta é uma boa hora de participar.\n\n" +
                "Atualize-se ainda, acerca das diferentes organizações e movimentos sociais do seu local e ainda, os órgãos de controle (tribunais de conta, controladoria) que acompanham os investimentos públicos.");
        mContExp7.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp7.setShowBottomLine(false);
        mContExp7.setShowBottomLine(true);

        mContExp8.setTitle("O que devo fazer caso verifique alguma irregularidade nas informações?");
        mContExp8.setContent(
                "Dependendo da situação, você tem diferentes órgãos de avaliação e controle a quem pode comunicar as irregularidades, que são:\n\n" +
                "1. Ao órgão de auditoria do SUS pelo site ou telefone do Ministério da Saúde;\n\n" +
                "2. À direção local do SUS;\n\n" +
                "3. Ao responsável pela administração orçamentária e financeira do ente federativo;\n\n" +
                "4. Aos órgãos de controle interno e externo do ente federativo;\n\n" +
                "5. Ao Conselho de Saúde; e\n\n" +
                "6. Ao Ministério Público.\n\n" +
                "7. Ao Tribunal de Contas");
        mContExp8.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp8.setShowBottomLine(true);

        mContExp9.setTitle("Por que as vezes eu não consigo fazer uma pesquisa para o ano atual?");
        mContExp9.setContent(
                "Porque as informações são disponibilizadas a partir do SIOPS, que por sua vez depende da alimentação do município, que tem prazos estabelecidos conforme a legislação vigente.\n\n" +
                "O município pode também estar em atraso e você não conseguir dados semestrais.\n\n" +
                "A Portaria nº 53/2012 do Ministério da Saúde estabeleceu: “Art. 12. A transmissão dos dados sobre receitas totais e despesas com ações e serviços públicos de saúde para o SIOPS deverá ser feita a cada bimestre por todos os entes da Federação, observadas as regras de cadastro e responsabilidade previstas (...)”.");
        mContExp9.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp9.setShowBottomLine(true);

        mContExp10.setTitle("De onde são retirados os valores exibidos na seção \"Valor por pessoa por dia e valores disponíveis por ano\"?");
        mContExp10.setContent(
                "Do menu \"Demonstrativos\" desta página do SIOPS\n" +
                "http://siops.datasus.gov.br\n\n" +
                "Para acessar, o usuário deverá clicar em \"+ Leia Mais\" e então em \"Demonstrativo da Saúde – RREO\" e selecionar o ente a ser consultado (União, Estados, DF ou Municípios), além do ano, UF, município e período.\n\n" +
                "Em seguida, deve clicar em \"Consultar\".");
        mContExp10.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp10.setShowBottomLine(true);

        mContExp11.setTitle("De onde são retirados os valores exibidos na seção \"Ranking dos municípios do Brasil\"?");
        mContExp11.setContent(
                "Da página do SIOPS\n" +
                "http://portalms.saude.gov.br/repasses-financeiros/siops/indicadores\n\n" +
                "Na seção: Indicadores > Municípios > Consulta por ano\n\n" +
                "Após preencher e confirmar o formulário você será redirecionado para esta página\n\n" +
                "http://siops.datasus.gov.br/consdetalhereenvio2.php que contém o relatório desejado.\n\n" +
                "Do relatório de Indicadores Municipais, é extraído o Indicador 2.1 - Despesa total com Saúde, em R$/hab, sob responsabilidade do Município\n\n" +
                "Os valores do indicador 2.1 de todos os municípios disponíveis no sistema foram coletados (e não sofreram qualquer alteração) considerando os mesmos parâmetros de busca inseridos pelo usuário e então um ranking com todos os municípios foi criado.\n\n" +
                "Para otimizar a visualização deste ranking no app, foi criado um resumo, organizado da seguinte forma:\n\n" +
                "  1. Maior investimento per capta municipal do país\n\n" +
                "  2. Maior investimento per capta estadual\n\n" +
                "  3. O município pesquisado\n\n" +
                "  4. Menor investimento per capta estadual\n\n" +
                "  5. Menor investimento per capta do país");
        mContExp11.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp11.setShowBottomLine(true);

        mContExp12.setTitle("De onde são retirados os valores exibidos na seção \"Investimentos em saúde pública\"?");
        mContExp12.setContent(
                "Da página do SIOPS\n" +
                "http://portalms.saude.gov.br/repasses-financeiros/siops/indicadores\n\n" +
                "Na seção: Demonstrativos Dados Informados > Demonstrativos > Municípios > Demonstrativos Saúde RREO\n\n\n" +
                "Após preencher e confirmar o formulário você será redirecionado para esta página\n\n" +
                "http://siops.datasus.gov.br/rel_LRF.php que contém o relatório desejado.");
        mContExp12.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp12.setShowBottomLine(true);

        mContExp13.setTitle("De onde são retirados os valores exibidos na seção \"Utilização dos recursos investidos\"?");
        mContExp13.setContent(
                "Da página do SIOPS\n" +
                "http://portalms.saude.gov.br/repasses-financeiros/siops/demonstrativos-dados-informados\n\n" +
                "Na seção: Demonstrativos Dados Informados > Demonstrativos > Municípios > Demonstrativos Saúde RREO\n\n" +
                "No relatório no SIOPS -Demonstrativo da Lei de Responsabilidade Fiscal, que contém o \"Relatório Resumido da Execução Orçamentária, Demonstrativo das Receitas e Despesas com Ações e Serviços públicos de Saúde, Orçamentos Fiscal e da Seguridade Social\" São extraídas as  despesas com Saúde (Por subfunção)\n\n" +
                "Como estas informações são processadas?\n\n" +
                "Os valores da coluna Despesas Empenhadas e da subcoluna Liquidadas Até o Bimestre são coletados, tratados e armazenados sem sofrer nenhuma alteração. A partir da soma de todos os investimentos coletados, são calculados os percentuais de cada item com relação ao todo e então eles são exibidos pelo app.");
        mContExp13.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp13.setShowBottomLine(true);

        mContExp14.setTitle("Quais os dispositivos legais atuais acerca do Siops?");
        mContExp14.setContent(
                "Lei Complementar n° 141/2012\n\n" +
                "http://www.planalto.gov.br/ccivil_03/leis/LCP/Lcp141.htm\n\n" +
                "Decreto n° 7827/2012\n\n" +
                "http://www2.camara.leg.br/legin/fed/decret/2012/decreto-7827-16-outubro-2012-774401-publicacaooriginal-137887-pe.html");
        mContExp14.setBackgroundColor(R.color.color_white, getContext().getResources());
        mContExp14.setShowBottomLine(false);
        mContExp14.hideShowBottomLine(true);

        content.add(mContExp1);
        content.add(mContExp2);
        content.add(mContExp3);
        content.add(mContExp4);
        content.add(mContExp5);
        content.add(mContExp6);
        content.add(mContExp7);
        content.add(mContExp8);
        content.add(mContExp9);
        content.add(mContExp10);
        content.add(mContExp11);
        content.add(mContExp12);
        content.add(mContExp13);
        content.add(mContExp14);
    }

    /**
     * Events
     */
    private void initEvents() {
        mBtnClose.setOnClickListener(mBtnCloseClickListener);
    }

    private final ComponentContentExpanderContract mContentExpanderListener =
            new ComponentContentExpanderContract() {
        @Override
        public void onContentToggled(boolean contentOpened, String title) {

            for (ComponentContentExpander cce : content) {

                if (contentOpened && !cce.getTitle().equals(title)) {
                    cce.closeContent(false);
                }
            }
        }
    };

    private final View.OnClickListener mBtnCloseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCallback != null) {
                mCallback.onCloseDialog();
            }
        }
    };

    public void closeDialog() {
        FAQDialog.this.hide();
    }

}